package wp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Post {
	private String category;

	private String title;

	private String content;

	private PostStatus status;

	private PostType type;

	private Date createdAt;

	private List<Comment> comments = new ArrayList<Comment>();

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public PostStatus getStatus() {
		return status;
	}

	public void setStatus(PostStatus status) {
		this.status = status;
	}

	public PostType getType() {
		return type;
	}

	public void setType(PostType type) {
		this.type = type;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public void addComment(Comment comment) {
		this.comments.add(comment);
	}

	public List<Comment> getComments() {
		return Collections.unmodifiableList(comments);
	}

	public String toString() {
		return String.format("{category: %s, title: %s, content: %s, status: %s, type: %s, createdAt: %s}", category, title, content, status, type, createdAt);
	}
}
