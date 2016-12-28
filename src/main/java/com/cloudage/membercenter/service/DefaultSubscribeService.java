package com.cloudage.membercenter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloudage.membercenter.entity.Shop;
import com.cloudage.membercenter.entity.Subscribe;
import com.cloudage.membercenter.entity.Subscribe.Key;
import com.cloudage.membercenter.entity.User;
import com.cloudage.membercenter.repository.ISubscribesRepository;

@Component
@Service
@Transactional
public class DefaultSubscribeService implements ISubscribeService{

        @Autowired
        ISubscribesRepository subscribeRepo;
        
        @Override
        public void addSubscribe(User user, Shop shop) {
                Subscribe.Key key = new Key();
                key.setUser(user);
                key.setShop(shop);
                Subscribe sb = new Subscribe();
                sb.setId(key);
                subscribeRepo.save(sb);
        }

        @Override
        public void removeSubscribe(User user, Shop shop) {
                Subscribe.Key key = new Key();
                key.setUser(user);
                key.setShop(shop);
                subscribeRepo.delete(key);
                
        }

        @Override
        public int countSubscribe(int shopId) {
                return subscribeRepo.subscribeCountsOfShop(shopId);
        }

        @Override
        public boolean checkSubscribed(int userId, int shopId) {
                return subscribeRepo.checkSubscribeExsists(userId, shopId)>0;
        }

}
