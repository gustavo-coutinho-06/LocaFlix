package locaflix.corporation.locaflix.application.auth;

import locaflix.corporation.locaflix.domain.model.Session;

public interface SessionTokenIssuer {

    String issue(Session session);

}
