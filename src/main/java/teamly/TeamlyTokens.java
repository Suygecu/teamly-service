package teamly;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TeamlyTokens {

	@JsonProperty("client_id")
	private String clientId;

	@JsonProperty("client_secret")
	private String clientSecret;

	@JsonProperty("refresh_token")
	private String refreshToken;

	private String access_token;

	private String expires_at;

	public TeamlyTokens(String clientId, String clientSecret, String refreshToken) {
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.refreshToken = refreshToken;
	}
}

