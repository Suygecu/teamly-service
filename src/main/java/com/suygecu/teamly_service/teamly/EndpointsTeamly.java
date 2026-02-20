package com.suygecu.teamly_service.teamly;


import lombok.Getter;
import org.springframework.http.HttpMethod;
import com.suygecu.teamly_service.dto.ArticleCreatePayload;
import com.suygecu.teamly_service.dto.GroupPayload;
import com.suygecu.teamly_service.dto.PropertyUpdatePayload;


@Getter
public enum EndpointsTeamly implements TeamlyEndpoint{
	
	COMMAND_EXECUTE("https://base.xexbo.ru/api/v1/wiki/properties/command/execute", HttpMethod.POST),     // - отправка команд для изменений
	CONTENT_TABLE("https://base.xexbo.ru/api/v1/ql/content-database/content", HttpMethod.POST),           // - получение умной таблицы, без вложенных строк
	NESTED_CONTENT("https://base.xexbo.ru/api/v1/ql/content-database/nestedContent", HttpMethod.POST),    // - получение значений дочерних строки умной таблицы
	ARTICLE_SPACE("https://base.xexbo.ru/api/v1/wiki/article", HttpMethod.POST),                          // - получение страницы со статьей
	SPACE_INFO("https://base.xexbo.ru/api/v1/wiki/ql/space", HttpMethod.POST),                            // - получение всей информации пространства в тимли
	AUTH_REFRESH("https://base.xexbo.ru/api/v1/auth/integration/refresh", HttpMethod.POST),               // - обновление токена авторизации
	AUTH_AUTHORIZE("https://base.xexbo.ru/api/v1/auth/integration/authorize", HttpMethod.POST),           // - получение токена авторизации
	;

	private final String url;

	EndpointsTeamly(String url, HttpMethod post) {

		this.url = url;
	}

	@Override
	public String path() {
		return "";
	}

	@Override
	public HttpMethod method() {
		return null;
	}


	@Getter
	public enum TeamlyCommand {
		ARTICLE_CREATE("article_create", COMMAND_EXECUTE.getUrl(), ArticleCreatePayload.class),
		PROPERTY_UPDATE("property_update", COMMAND_EXECUTE.getUrl(), PropertyUpdatePayload.class),
		GROUP("group", COMMAND_EXECUTE.getUrl(), GroupPayload.class);

		private final String code;
		private final String endpoint;
		private final Class<?> payloadClass;

		TeamlyCommand(String code, String endpoint, Class<?> payloadClass) {
			this.code = code;
			this.endpoint = endpoint;
			this.payloadClass = payloadClass;
		}

	}

}

