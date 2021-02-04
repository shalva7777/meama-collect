package com.meama.atom.categories.storage;

import com.meama.atom.categories.storage.model.Category;
import com.meama.common.MathUtils;
import com.meama.common.response.ListResult;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
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
        Map<Long, List<Category>> map = new HashMap<>();
        Map<Long, Category> objectMap = new HashMap<>();
        List<Category> resultList = q.getResultList();
        for (Category category : resultList) {
            if (category.getParentCategoryId() == null) {
                map.put(category.getId(), new ArrayList<>());
                objectMap.put(category.getId(), category);
            } else {
                if (map.containsKey(category.getParentCategoryId())) {
                    List<Category> categories = map.get(category.getParentCategoryId());
                    categories.add(category);
                    map.put(category.getParentCategoryId(), categories);
                }
            }
        }
        if (map.size() > 0) {
            resultList = new ArrayList<>();
        }
        for (Map.Entry<Long, List<Category>> entry : map.entrySet()) {
            resultList.add(objectMap.get(entry.getKey()));
            resultList.addAll(entry.getValue());
        }
        ListResult<Category> result = new ListResult<>();
        result.setResultList(resultList);
        result.setPage(offset / limit);
        result.setOffset(offset);
        result.setLimit(limit);
        result.setCount((Long) cq.getSingleResult());
        result.setPageNum(MathUtils.calculatePageNum(result.getCount(), result.getLimit()));
        return result;
    }

    public ListResult<Category> find(Long id, Long equalId, String name, Boolean active, int limit, int offset, String orderBy, boolean asc) {
        StringBuilder builder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        if (id != null) {
            builder.append(" AND c.id = :id");
            params.put("id", id);
        }
        if (equalId != null) {
            builder.append(" AND c.id <> :equalId");
            params.put("equalId", equalId);
            builder.append(" AND c.parentCategoryId is null ");
        }
        if (!StringUtils.isEmpty(name)) {
            builder.append(" AND LOWER(c.name) LIKE LOWER(:name)");
            params.put("name", "%" + name + "%");
        }
        if (active != null) {
            builder.append(" AND c.active = :active");
            params.put("active", active);
        }
        StringBuilder order = new StringBuilder();
        if (orderBy != null) {
            order.append(" ORDER BY c.").append(orderBy).append(" ").append(asc ? "ASC" : "DESC");
        } else {
            order.append(" ORDER BY c.id DESC");
        }
        TypedQuery<Category> q = em.createQuery("SELECT c FROM Category c WHERE 1=1 " + builder.toString() + order.toString(), Category.class);
        Query cq = em.createQuery("SELECT COUNT(c.id) FROM Category c WHERE 1=1 " + builder.toString());
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

    public List<Category> findCategoriesForNewCategory(Long equalId) {
        StringBuilder builder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        if (equalId != null) {
            builder.append(" AND c.id <> :equalId");
            params.put("equalId", equalId);
        }
        builder.append(" AND c.parentCategoryId is null ");
        builder.append(" AND c.active = true ");
        TypedQuery<Category> q = em.createQuery("SELECT c FROM Category c WHERE 1=1 " + builder.toString(), Category.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        List<Category> categories = new ArrayList<>();
        List<Category> resultList = q.getResultList();
        for (Category category : resultList) {
            long count = countUsedParentCategory(category.getParentCategoryId());
            if (count == 0) {
                categories.add(category);
            }
        }
        return categories;
    }

    public Long countUsedParentCategory(Long parentCategoryId) {
        StringBuilder builder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        if (parentCategoryId != null) {
            builder.append(" AND c.parentCategoryId = :parentCategoryId");
            params.put("parentCategoryId", parentCategoryId);
        }
        Query cq = em.createQuery("SELECT COUNT(c.id) FROM Category c WHERE 1=1 " + builder.toString());
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            cq.setParameter(entry.getKey(), entry.getValue());
        }
        return (Long) cq.getSingleResult();
    }

    public Long countById(Long Id) {
        StringBuilder builder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        if (Id != null) {
            builder.append(" AND c.id = :Id");
            params.put("Id", Id);
        }
        Query cq = em.createQuery("SELECT COUNT(c.id) FROM Category c WHERE 1=1 " + builder.toString());
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            cq.setParameter(entry.getKey(), entry.getValue());
        }
        return (Long) cq.getSingleResult();
    }
}
