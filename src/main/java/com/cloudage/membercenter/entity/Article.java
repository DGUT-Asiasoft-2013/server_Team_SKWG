package com.cloudage.membercenter.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;

import com.cloudage.membercenter.util.BaseEntity;
import com.cloudage.membercenter.util.DateRecord;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Article extends DateRecord {
        User author;
        String title;
        String text;
        String articlesImage;
        int commentNum;
        

        public int getCommentNum() {
			return commentNum;
		}

		public void setCommentNum(int commentNum) {
			this.commentNum = commentNum;
		}

		public String getArticlesImage() {
			return articlesImage;
		}

		public void setArticlesImage(String articlesImage) {
			this.articlesImage = articlesImage;
		}
		public void addArticlesImage(String add) {
			this.articlesImage = this.articlesImage + add;
		}

		@ManyToOne(optional = false)
        @JsonIgnore
        public User getAuthor() {
                return author;
        }

        public void setAuthor(User author) {
                this.author = author;
        }

        @Transient
        public String getAuthorName() {
                return author.name;
        }

        @Transient
        public String getAuthorAvatar() {
                return author.avatar;
        }

        public String getTitle() {
                return title;
        }

        public void setTitle(String title) {
                this.title = title;
        }

        public String getText() {
                return text;
        }

        public void setText(String text) {
                this.text = text;
        }

}
