package ishop.service.impl;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.FacebookClient.AccessToken;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.scope.FacebookPermissions;
import com.restfb.scope.ScopeBuilder;
import com.restfb.types.User;

import ishop.model.SocialAccount;
import ishop.service.SocialService;

public class FacebookSocialService implements SocialService {
	private final String idClient;
	private final String secret;
	private final String redirectUrl;

	FacebookSocialService(ServiceManager serviceManager) {
		super();
		this.idClient = serviceManager.getApplicationProperties("social.facebook.idClient");
		this.secret = serviceManager.getApplicationProperties("social.facebook.secret");
		this.redirectUrl = serviceManager.getApplicationProperties("app.host") + "/from-social";
	}

	@Override
	public String getAuthorizeUrl() {
		ScopeBuilder scopeBuilder = new ScopeBuilder();
		scopeBuilder.addPermission(FacebookPermissions.EMAIL);
		FacebookClient client = new DefaultFacebookClient(Version.VERSION_3_1);
		return client.getLoginDialogUrl(idClient, redirectUrl, scopeBuilder);

	}

	@Override
	public SocialAccount getSocialAccount(String authToken) {
		FacebookClient client = new DefaultFacebookClient(Version.VERSION_3_1);
		AccessToken accessToken = client.obtainUserAccessToken(idClient, secret, redirectUrl, authToken);
		client = new DefaultFacebookClient(accessToken.getAccessToken(), Version.VERSION_3_1);
		User user = client.fetchObject("me", User.class, Parameter.with("fields", "name,email,first_name,last_name"));
		String avatarUrl = String.format("https://graph.facebook.com/v2.7/%s/picture?type=small", user.getId());
		return new SocialAccount(user.getName(), user.getEmail(), avatarUrl);
	}

}
