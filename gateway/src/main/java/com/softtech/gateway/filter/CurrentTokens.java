package com.softtech.gateway.filter;

import com.softtech.gateway.exceptionhandling.GlobalRuntimeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CurrentTokens {
    private TokenQueue queue=new TokenQueue();  //çift yönlü dairesel sıra
    private HashMap<String, String> tokenMap = new HashMap<>();      // token & role
    private HashMap<String,Integer> multiLogins = new HashMap<>();
    private Timer timer = new Timer();  // farklı bir thread için...


    @Value("${jwt.TOKEN_DURATION}")
    private Long TOKEN_DURATION;



    private final int tryLoginLimit = 5;  // token duration icerisinde 5 kere denerse blocklansın

    private TimerTask task = new TimerTask() {
        @Override
        public void run() {
            killDeadTokens();
            System.out.println("[!] Olu tokenler kontrol edildi.");
        }
    };

    public CurrentTokens() {
        timer.schedule(task, 10000, 10000);

    }

    public void addToCache(String token, String userName, String role){
        if(tokenMap.containsKey(userName)){
            if(countSessions(userName)==tryLoginLimit){
                //kötü niyetli bir kullanıcı için...
                //limite ulaştı ise veri tabanında blockla hashmapden sil
                multiLogins.remove(userName);
                tokenMap.remove(userName);
                System.out.println("[!] Kullanıcı blocklandı: "+userName);
                throw new GlobalRuntimeException("Suspicious move. You cannot login for 24 hours.", HttpStatus.TOO_MANY_REQUESTS) {
                };
            }
        }
        addToQueue(token,userName);
        tokenMap.put(token,role);
    }


    public HashMap<String, String> getTokenMap() {
        return tokenMap;
    }

    private void addToQueue(String token,String userName){
        if(queue.getData()==null){
            //ilk token
            queue.setData(token);
            queue.setUserName(userName);
            queue.setInitialDate(new Date(System.currentTimeMillis()));
            queue.next=queue;
            queue.prev=queue;
        }else if(queue.prev==queue){
            //tek token var
            TokenQueue iter=new TokenQueue(token,userName,new Date(System.currentTimeMillis()));
            queue.next=iter;
            queue.prev=iter;
            iter.prev=queue;
            iter.next=queue;
        }else{
            //en az 2 token var
            TokenQueue iter=new TokenQueue(token,userName,new Date(System.currentTimeMillis()));
            queue.prev.next=iter;
            iter.prev=queue.prev;
            queue.prev=iter;
            iter.next=queue;
        }
    }

    private void removeFromQueue(){
        if(queue.getData()!=null){
            // 0 token degil...
            if(queue.next==queue){
                // 1 token...
                queue.setInitialDate(null);
                queue.setData(null);
                queue.setUserName(null);
                queue.next=null;
                queue.prev=null;
            }else{
                // 1 den fazla token...
                TokenQueue iter=queue.next;
                queue.prev.next=iter;
                iter.prev=queue.prev;
                queue=iter;
            }
        }
    }

    private void killDeadTokens(){
        Date killTime = new Date(System.currentTimeMillis()- TOKEN_DURATION);
        if(queue.getInitialDate()!=null && queue.getInitialDate().before(killTime)){
            //kuyrugun basını kontrol et burdan silinen olursa hashmap den de sil
            System.out.println("[!] Silinen token: "+queue.getData());
            tokenMap.remove(queue.getUserName(),queue.getData());
            //multi login yaptı ise sayısını 1 düşür
            if(multiLogins.containsKey(queue.getUserName())){
                int count = multiLogins.get(queue.getUserName());
                if(count==1){
                    multiLogins.remove(queue.getUserName());
                    System.out.println("[!] Kullanıcı: "+queue.getUserName()+" için oturum sayısı: "+0);
                }else{
                    multiLogins.put(queue.getUserName(),--count);
                    System.out.println("[!] Kullanıcı: "+queue.getUserName()+" için oturum sayısı: "+count);
                }
            }
            removeFromQueue();
            killDeadTokens();   //bir sonrakini de kontrol et
        }
    }

    //silinecek
    public List<String> listQueue(){
        List<String> listQueue = new ArrayList<>();
        if(queue.getData()!=null){
            TokenQueue iter = queue;
            listQueue.add(queue.getData());
            while(iter.next!=queue){
                iter=iter.next;
                listQueue.add(iter.getData());
            }
        }
        return listQueue;
    }

    public String  getRoleOfToken(String token){
        return tokenMap.get(token);
    }

    private int countSessions(String userName){
        if(multiLogins.containsKey(userName)){
            int session = multiLogins.get(userName);
            multiLogins.put(userName,++session);
            System.out.println("[!] Kullanıcı: "+userName+" için oturum sayısı: "+session);
            return session;
        }else{
            System.out.println("[!] Kullanıcı: "+userName+" için oturum sayısı: "+2);
            multiLogins.put(userName,2);
            return 2;
        }
    }

    public void removeFromCache(String token) {
        tokenMap.remove(token);
        System.out.println("[!] Token cache den kaldırıldı: "+token);
    }
}