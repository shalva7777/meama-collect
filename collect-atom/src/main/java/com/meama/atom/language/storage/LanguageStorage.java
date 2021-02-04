package com.meama.atom.language.storage;

import com.meama.atom.language.storage.model.Language;
import com.meama.common.MathUtils;
import com.meama.common.response.ListResult;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class LanguageStorage {

    @PersistenceContext
    private EntityManager em;

    public ListResult<Language> find(String query, int limit, int offset, String orderBy, boolean asc) {
        StringBuilder builder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        if (query != null && query.length() >= 2) {
            builder.append(" WHERE LOWER(l.code) LIKE LOWER(:query)");
            builder.append(" OR LOWER(l.name) LIKE LOWER(:query)");
            params.put("query", "%" + query.toLowerCase() + "%");
        }
        StringBuilder order = new StringBuilder();
        if (orderBy != null) {
            order.append(" ORDER BY l.").append(orderBy).append(" ").append(asc ? "ASC" : "DESC");
        } else {
            order.append(" ORDER BY l.id DESC");
        }
        TypedQuery<Language> q = em.createQuery("SELECT l FROM Language l " + builder.toString() + order.toString(), Language.class);
        Query cq = em.createQuery("SELECT COUNT(l.id) FROM Language l " + builder.toString());
        if (!(limit == -1 && offset == -1)) {
            q.setFirstResult(offset);
            q.setMaxResults(limit);
        }
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
            cq.setParameter(entry.getKey(), entry.getValue());
        }
        List<Language> resultList = q.getResultList();
        ListResult<Language> result = new ListResult<>();
        result.setResultList(resultList);
        result.setPage(offset / limit);
        result.setOffset(offset);
        result.setLimit(limit);
        result.setCount((Long) cq.getSingleResult());
        result.setPageNum(MathUtils.calculatePageNum(result.getCount(), result.getLimit()));
        return result;
    }

}
