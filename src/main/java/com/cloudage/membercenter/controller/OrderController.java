package com.cloudage.membercenter.controller;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cloudage.membercenter.entity.Goods;
import com.cloudage.membercenter.entity.Orders;
import com.cloudage.membercenter.entity.User;
import com.cloudage.membercenter.service.IArticleService;
import com.cloudage.membercenter.service.ICommentService;
import com.cloudage.membercenter.service.IGoodsService;
import com.cloudage.membercenter.service.ILikesService;
import com.cloudage.membercenter.service.IOrdersService;
import com.cloudage.membercenter.service.IUserService;

@RestController
@RequestMapping("/api")
public class OrderController {
	@Autowired
	IUserService userService;

	@Autowired
	IGoodsService goodsService;

	@Autowired
	IOrdersService ordersService;

	@Autowired
	UserController userController;

	// 生成订单
	@RequestMapping(value = "/orders", method = RequestMethod.POST)
	public Orders addOrders(@RequestParam String ordersID, @RequestParam int ordersState,
			@RequestParam int goodsQTY, @RequestParam double goodsSum, @RequestParam String buyerName,
			@RequestParam String buyerPhoneNum, @RequestParam String buyerAddress,
			@RequestParam String postCode,
			@RequestParam int goodsId, HttpServletRequest request) {

		//double goodsSums= Double.parseDouble(goodsSum);
		User me = userController.getCurrentUser(request);
		Goods goods = goodsService.findById(goodsId);

		Orders orders = new Orders();
		orders.setOrdersID(ordersID);
		orders.setOrdersState(ordersState);
		orders.setBuyer(me);
		orders.setBuyerName(buyerName);
		orders.setBuyerPhoneNum(buyerPhoneNum);
		orders.setBuyerAddress(buyerAddress);
		orders.setGoods(goods);
		orders.setGoodsQTY(goodsQTY);
		orders.setGoodsSum(goodsSum);
		orders.setPostCode(postCode);
		return ordersService.save(orders);
	}

	@RequestMapping("/orders/ordersOfSeller")
	public Page<Orders> getOrdersOfSeller(@RequestParam(defaultValue = "0") int page, HttpServletRequest request) {
		User me = userController.getCurrentUser(request);
		return ordersService.findAllBySellerId(me.getId(), page);
	}

	@RequestMapping("/orders/ordersOfBuyer")
	public Page<Orders> getOrdersOfBuyer(@RequestParam(defaultValue = "0") int page, HttpServletRequest request) {
		User me = userController.getCurrentUser(request);
		return ordersService.findAllByBuyerId(me.getId(), page);
	}

	@RequestMapping(value = "/orders/getorders/{orders_id}")
	public Orders getOrdersOfOrdersID(@RequestParam String orders_id) {
		return ordersService.findOrdersByOrdersID(orders_id);
	}

	// 根据订单状态获取当前用户的订单
	@RequestMapping(value = "/orders/findall/{state}")
	public Page<Orders> getOrdersOfMeWithOrdersState(@PathVariable int state, @RequestParam int page,
			HttpServletRequest request) {
		User me = userController.getCurrentUser(request);
		return ordersService.findAllofMineWithState(me.getId(), state, page);
	}

	@RequestMapping(value="/orders/delete", method = RequestMethod.POST)
	public void deleteOrders(@RequestParam String ordersID, HttpServletRequest request) {
		User me = userController.getCurrentUser(request);
		Orders orders = ordersService.findOrdersByOrdersID(ordersID);
		ordersService.deleteOrders(orders);
	}

	//获取当前用户的所有订单
	@RequestMapping(value="/orders/findall")
	public Page<Orders> findAllOfUser(HttpServletRequest request,
			@RequestParam(defaultValue="0") int page){
		User me=userController.getCurrentUser(request);
		return ordersService.findAllOfMine(me.getId(),page);
	}
	//删除选中的订单
	@RequestMapping(value="/orders/delete/{orders_id}")
	public boolean deleteOrder(@PathVariable String orders_id){
		Orders orders = getOrdersOfOrdersID(orders_id);
		ordersService.deleteOrders(orders);
		return true;
	}

	// 修改订单状态
	@RequestMapping(value="/order/{orders_id}")
	public void changeStateByOrdersId(@PathVariable String orders_id, @RequestParam int state) {
		Orders orders = getOrdersOfOrdersID(orders_id);
		orders.setOrdersState(state);
		ordersService.save(orders);
	}

	//支付订单
	@RequestMapping(value="/order/payfor/{orders_id}")
	public boolean  payForOrders(@PathVariable String orders_id, @RequestParam int state,
			HttpServletRequest request) {
		Orders orders = getOrdersOfOrdersID(orders_id);
		orders.setOrdersState(state);
		ordersService.save(orders);
		User me=userController.getCurrentUser(request);
		me.setMoney(me.getMoney()-orders.getGoodsSum());
		userService.save(me);

		Goods goods =orders.getGoods();
		goods.setGoodsCount(goods.getGoodsCount()-orders.getGoodsQTY());   
		goodsService.save(goods);
		if(orders.getOrdersState()==3){
			return true;
		}
		else{
			return false;
		}
	}




}
