package com.meama.security.user.storage;

import com.meama.common.MathUtils;
import com.meama.common.response.ListResult;
import com.meama.security.user.storage.model.User;
import com.meama.security.user.storage.model.UserType;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserStorage {

    @PersistenceContext
    private EntityManager em;

    public ListResult<User> find(String query, int limit, int offset, String orderBy, boolean asc) {
        StringBuilder builder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        if (query != null && query.length() >= 3) {
            builder.append(" WHERE LOWER(u.username) LIKE :query");
            builder.append(" OR LOWER(u.firstName) LIKE :query");
            builder.append(" OR LOWER(u.lastName) LIKE :query");
            builder.append(" OR LOWER(u.email) LIKE :query");
            builder.append(" OR LOWER(u.phone) LIKE :query");
            params.put("query", "%" + query.toLowerCase() + "%");
        }
        StringBuilder order = new StringBuilder();
        if (orderBy != null) {
            order.append(" ORDER BY u.").append(orderBy).append(" ").append(asc ? "ASC" : "DESC");
        } else {
            order.append(" ORDER BY u.id DESC");
        }
        TypedQuery<User> q = em.createQuery("SELECT u FROM User u " + builder.toString() + order.toString(), User.class);
        Query cq = em.createQuery("SELECT COUNT(u.id) FROM User u " + builder.toString());
        if (!(limit == -1 && offset == -1)) {
            q.setFirstResult(offset);
            q.setMaxResults(limit);
        }
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
            cq.setParameter(entry.getKey(), entry.getValue());
        }
        List<User> resultList = q.getResultList();
        ListResult<User> result = new ListResult<>();
        result.setResultList(resultList);
        result.setPage(offset / limit);
        result.setOffset(offset);
        result.setLimit(limit);
        result.setCount((Long) cq.getSingleResult());
        result.setPageNum(MathUtils.calculatePageNum(result.getCount(), result.getLimit()));
        return result;
    }

    public List<String> findUsernamesByRole(String role) {
        TypedQuery<String> q = em.createQuery("SELECT u.username FROM User u WHERE EXISTS (SELECT r FROM u.roles r WHERE LOWER(r.name) = :role) ORDER BY u.id DESC", String.class);
        q.setParameter("role", role.toLowerCase());
        return q.getResultList();
    }

    public ListResult<User> findByFilters(Long id, String username, String firstName, String lastName, String sapCode, String email, String phone, String personalId, Boolean isActive,
                                          Date fromCreationDate, Date toCreationDate, Date fromModificationDate, Date toModificationDate, Long roleId,
                                          UserType userType, Integer fromRating, Integer toRating, int limit, int offset, String orderBy, boolean asc, Long companyId) {
        StringBuilder builder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        if (id != null) {
            builder.append(" AND u.id = :id");
            params.put("id", id);
        }
        if (username != null && !StringUtils.isEmpty(username)) {
            builder.append(" AND LOWER(u.username) LIKE LOWER(:username)");
            params.put("username", "%" + username + "%");
        }
        if (firstName != null && !StringUtils.isEmpty(firstName)) {
            builder.append(" AND LOWER(u.firstName) LIKE LOWER(:firstName)");
            params.put("firstName", "%" + firstName + "%");
        }
        if (lastName != null && !StringUtils.isEmpty(lastName)) {
            builder.append(" AND LOWER(u.lastName) LIKE LOWER(:lastName)");
            params.put("lastName", "%" + lastName + "%");
        }
        if (email != null && !StringUtils.isEmpty(email)) {
            builder.append(" AND LOWER(u.email) LIKE LOWER(:email)");
            params.put("email", "%" + email + "%");
        }
        if (phone != null && !StringUtils.isEmpty(phone)) {
            builder.append(" AND u.phone = :phone");
            params.put("phone", phone);
        }
        if (!StringUtils.isEmpty(sapCode)) {
            builder.append(" AND u.sapCode = :sapCode");
            params.put("sapCode", sapCode);
        }
        if (personalId != null && !StringUtils.isEmpty(personalId)) {
            builder.append(" AND u.personalId = :personalId");
            params.put("personalId", personalId);
        }
        if (isActive != null) {
            builder.append(" AND u.active = :active");
            params.put("active", isActive);
        }
        if (fromCreationDate != null) {
            builder.append(" AND u.creationDate >= :fromCreationDate");
            params.put("fromCreationDate", fromCreationDate);
        }
        if (toCreationDate != null) {
            builder.append(" AND u.creationDate <= :toCreationDate");
            params.put("toCreationDate", toCreationDate);
        }
        if (fromModificationDate != null) {
            builder.append(" AND u.modificationDate >= :fromModificationDate");
            params.put("fromModificationDate", fromModificationDate);
        }
        if (toModificationDate != null) {
            builder.append(" AND u.modificationDate <= :toModificationDate");
            params.put("toModificationDate", toModificationDate);
        }
        if (fromRating != null) {
            builder.append(" AND u.rating >= :fromRating");
            params.put("fromRating", fromRating);
        }
        if (toRating != null) {
            builder.append(" AND u.rating <= :toRating");
            params.put("toRating", toRating);
        }
        if (roleId != null) {
            builder.append(" AND EXISTS (SELECT r FROM u.roles r WHERE r.id = :roleId)");
            params.put("roleId", roleId);
        }
        if (userType != null) {
            builder.append(" AND u.userType = :userType");
            params.put("userType", userType);
        }
        if (companyId != null) {
            builder.append(" AND u.company.id = :company_id");
            params.put("company_id", companyId);
        }
        StringBuilder order = new StringBuilder();
        if (orderBy != null) {
            order.append(" ORDER BY u.").append(orderBy).append(" ").append(asc ? "ASC" : "DESC");
        } else {
            order.append(" ORDER BY u.id DESC");
        }

        TypedQuery<User> q = em.createQuery("SELECT u FROM User u WHERE 1=1 " + builder.toString() + order.toString(), User.class);
        Query cq = em.createQuery("SELECT COUNT(u.id) FROM User u WHERE 1=1 " + builder.toString());
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
            cq.setParameter(entry.getKey(), entry.getValue());
        }
        if (!(limit == -1 && offset == -1)) {
            q.setFirstResult(offset);
            q.setMaxResults(limit);
        }
        List<User> resultList = q.getResultList();
        ListResult<User> result = new ListResult<>();
        result.setResultList(resultList);
        result.setPage(offset / limit);
        result.setOffset(offset);
        result.setLimit(limit);
        result.setCount((Long) cq.getSingleResult());
        result.setPageNum(MathUtils.calculatePageNum(result.getCount(), result.getLimit()));
        return result;
    }

    public ListResult<User> find(String query, Long companyId, UserType userType, int limit, int offset, String orderBy, boolean asc) {
        StringBuilder builder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        if (query != null && query.length() >= 3) {
            builder.append(" AND (LOWER(u.username) LIKE :query");
            builder.append(" OR LOWER(u.firstName) LIKE :query");
            builder.append(" OR LOWER(u.lastName) LIKE :query");
            builder.append(" OR LOWER(u.email) LIKE :query");
            builder.append(" OR LOWER(u.phone) LIKE :query)");
            params.put("query", "%" + query.toLowerCase() + "%");
        }
        if (companyId != null) {
            builder.append(" AND u.company.id = :company_id");
            params.put("company_id", companyId);
        }
        StringBuilder order = new StringBuilder();
        if (orderBy != null) {
            order.append(" ORDER BY u.").append(orderBy).append(" ").append(asc ? "ASC" : "DESC");
        } else {
            order.append(" ORDER BY u.id DESC");
        }
        TypedQuery<User> q = em.createQuery("SELECT u FROM User u WHERE 1=1 " + builder.toString() + " AND u.userType = :userType " + order.toString(), User.class);
        Query cq = em.createQuery("SELECT COUNT(u.id) FROM User u WHERE 1=1 " + builder.toString() + " AND u.userType = :userType");
        params.put("userType", userType);
        if (!(limit == -1 && offset == -1)) {
            q.setFirstResult(offset);
            q.setMaxResults(limit);
        }
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
            cq.setParameter(entry.getKey(), entry.getValue());
        }
        List<User> resultList = q.getResultList();
        ListResult<User> result = new ListResult<>();
        result.setResultList(resultList);
        result.setPage(offset / limit);
        result.setOffset(offset);
        result.setLimit(limit);
        result.setCount((Long) cq.getSingleResult());
        result.setPageNum(MathUtils.calculatePageNum(result.getCount(), result.getLimit()));
        return result;
    }

    public ListResult<User> findUsersWithoutDepartment(String query, int limit, int offset) {
        StringBuilder builder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        builder.append(" WHERE NOT EXISTS (SELECT d FROM Department d WHERE u.id in (SELECT user.id FROM d.users user)) AND u.userType = :userType");
        params.put("userType", UserType.SYSTEM_USER);
        if (query != null && query.length() >= 3) {
            builder.append(" AND (LOWER(u.firstName) LIKE :query");
            builder.append(" OR LOWER(u.lastName) LIKE :query");
            builder.append(" OR LOWER(u.email) LIKE :query");
            builder.append(" OR LOWER(u.phone) LIKE :query)");
            params.put("query", "%" + query.toLowerCase() + "%");
        }
        TypedQuery<User> q = em.createQuery("SELECT u FROM User u " + builder.toString() + " ORDER BY u.id DESC", User.class);
        Query cq = em.createQuery("SELECT COUNT(u.id) FROM User u " + builder.toString());
        if (!(limit == -1 && offset == -1)) {
            q.setFirstResult(offset);
            q.setMaxResults(limit);
        }
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
            cq.setParameter(entry.getKey(), entry.getValue());
        }
        List<User> resultList = q.getResultList();
        ListResult<User> result = new ListResult<>();
        result.setResultList(resultList);
        result.setPage(offset / limit);
        result.setOffset(offset);
        result.setLimit(limit);
        result.setCount((Long) cq.getSingleResult());
        result.setPageNum(MathUtils.calculatePageNum(result.getCount(), result.getLimit()));
        return result;
    }
}
