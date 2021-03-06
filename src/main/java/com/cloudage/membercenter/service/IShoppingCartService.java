package com.cloudage.membercenter.service;

import org.springframework.data.domain.Page;

import com.cloudage.membercenter.entity.Goods;
import com.cloudage.membercenter.entity.ShoppingCart;
import com.cloudage.membercenter.entity.ShoppingCart.Key;
import com.cloudage.membercenter.entity.User;

public interface IShoppingCartService {
	ShoppingCart save(ShoppingCart cart);
	void delete(Key cartKey);
	ShoppingCart findById(int cartId);
	void addShoppingCart(User buyer, Goods goods, int quantity);
	boolean checkCart(int userId, int goodsId);
	ShoppingCart findById(int userId, int goodsId);
	Page<ShoppingCart> findAllByUserId(int userId, int page);
}
