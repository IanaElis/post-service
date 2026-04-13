package com.iana.postservice.repositories;

import com.iana.postservice.entities.Page;
import com.iana.postservice.entities.enums.PageType;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class PageRepository implements PanacheRepository<Page> {
    // find by parentPage id: all department pages for a faculty
    public List<Page> findByParentPageId(Integer parentId) {
        return find("parentPage.id", parentId).list();
    }

    // find by pageType
    public List<Page> findByPageType(PageType pageType) {
        return find("pageType = ?1 order by title asc", pageType).list();
    }

    //find by faculty/department
    public Page findByName(String name) {
        return find("name", name).firstResult();
    }

    public int followersCount(Integer pageId) {
        Integer count = find("page.id", pageId).project(Integer.class).firstResult();
        return count != null ? count : 0;
    }

    public boolean exists(Integer pageId) {
        return count("page.id = ?1", pageId) > 0;
    }

}
