package wp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Blog {
	private List<String> categories = new ArrayList<String>();

	private List<Post> posts = new ArrayList<Post>();

	public void addCategory(String category) {
		this.categories.add(category);
	}

	public void addPost(Post post) {
		this.posts.add(post);
	}

	public List<String> getCategories() {
		return Collections.unmodifiableList(categories);
	}

	public List<Post> getPosts() {
		return Collections.unmodifiableList(posts);
	}

	public String toString() {
		StringBuilder categories = new StringBuilder();
		for (String a : this.categories) {
			if (categories.length() > 0) {
				categories.append(", ");
			}
			categories.append(a);
		}
		return String.format("{categories: [%s]}", categories);
	}
}
