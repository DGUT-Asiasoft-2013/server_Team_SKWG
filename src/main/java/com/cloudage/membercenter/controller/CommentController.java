package com.cloudage.membercenter.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cloudage.membercenter.entity.Article;
import com.cloudage.membercenter.entity.Comment;
import com.cloudage.membercenter.entity.User;
import com.cloudage.membercenter.service.IArticleService;
import com.cloudage.membercenter.service.ICommentService;

@RestController
@RequestMapping("/api")
public class CommentController {
	
	@Autowired
	ICommentService commentService;
	
	@Autowired
    IArticleService articleService;
	
	@Autowired
	 UserController userController;
	
	@RequestMapping("/article/{article_id}/comments/{page}")
	public Page<Comment> getCommentsOfArticle(@PathVariable int article_id, @PathVariable int page) {
		return commentService.findCommentOfArticle(article_id, page);
	}

	@RequestMapping("/article/{article_id}/comments")
	public Page<Comment> getCommentsOfArticle(@PathVariable int article_id) {
		return commentService.findCommentOfArticle(article_id, 0);
	}

	@RequestMapping("/comments")
	public Page<Comment> getCommentsOfAuthor(@RequestParam(defaultValue = "0") int page,
			HttpServletRequest request) {
		User me = userController.getCurrentUser(request);
		return commentService.findAllOfAuthorId(me.getId(), page);
	}

	@RequestMapping("/mycomments")
	public Page<Comment> getCommentOfUser(@RequestParam(defaultValue = "0") int page, HttpServletRequest request) {
		User me = userController.getCurrentUser(request);
		return commentService.findAllOfUserId(me.getId(), page);
	}

	@RequestMapping(value = "/article/{article_id}/comments", method = RequestMethod.POST)
	public Comment postComment(@RequestParam String text, @PathVariable int article_id,
			HttpServletRequest request) {
		User me = userController.getCurrentUser(request);
		Article article = articleService.findOne(article_id);
		int commentnum=article.getCommentNum()+1;
		article.setCommentNum(commentnum);
		Comment comment = new Comment();
		comment.setAuthor(me);
		comment.setArticle(article);
		comment.setText(text);
		articleService.save(article);
		return commentService.save(comment);
	}
	
	@RequestMapping("/article/{article_id}/comments/count")
	public int getCommentCountOfArticle(@PathVariable int article_id){
		return commentService.getCommentCountOfArticle(article_id);
	}
	
	@Modifying
	@RequestMapping(value="/article/{article_id}/deletecomment",method=RequestMethod.DELETE)
	public int deleteCommentByArticleId(@PathVariable int article_id){
		return commentService.deleteCommentByArticleId(article_id);
	}
	
	@Modifying
	@RequestMapping(value="/deletecomment/{comment_id}",method=RequestMethod.DELETE)
	public int deleteCommentById(@PathVariable int comment_id){
		Comment comment = commentService.findOne(comment_id);
		int article_id=comment.getArticle().getId();
		Article article=articleService.findOne(article_id);
		int commentnum=article.getCommentNum()-1;
		article.setCommentNum(commentnum);
		articleService.save(article);
		return commentService.deleteCommentById(comment_id);
	}
}
