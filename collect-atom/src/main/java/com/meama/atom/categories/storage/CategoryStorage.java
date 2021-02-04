package com.meama.atom.categories.storage;

import com.meama.atom.categories.storage.model.Category;
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
public class CategoryStorage {

    @PersistenceContext
    private EntityManager em;

    public ListResult<Category> find(String query, int limit, int offset, String orderBy, boolean asc) {
        StringBuilder builder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        if (query != null && query.length() >= 3) {
            builder.append(" WHERE LOWER(c.name) LIKE LOWER(:query)");
            params.put("query", "%" + query.toLowerCase() + "%");
        }
        StringBuilder order = new StringBuilder();
        if (orderBy != null) {
            order.append(" ORDER BY c.").append(orderBy).append(" ").append(asc ? "ASC" : "DESC");
        } else {
            order.append(" ORDER BY c.id DESC");
        }
        TypedQuery<Category> q = em.createQuery("SELECT c FROM Category c " + builder.toString() + order.toString(), Category.class);
        Query cq = em.createQuery("SELECT COUNT(c.id) FROM Category c " + builder.toString());
        if (!(limit == -1 && offset == -1)) {
            q.setFirstResult(offset);
            q.setMaxResults(limit);
        }
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
            cq.setParameter(entry.getKey(), entry.getValue());
        }

        List<Category> resultList = q.getResultList();
        ListResult<Category> result = new ListResult<>();
        result.setResultList(resultList);
        result.setPage(offset / limit);
        result.setOffset(offset);
        result.setLimit(limit);
        result.setCount((Long) cq.getSingleResult());
        result.setPageNum(MathUtils.calculatePageNum(result.getCount(), result.getLimit()));
        return result;
    }
}
