package com.epam.esm.repository;

import java.util.List;

import com.epam.esm.entity.Tag;

public interface TagRepository extends CrdOperations<Tag> {

    public Tag getMostWidelyUsedUsersTag(long userId);

    public List<Tag> findByName(String tagName);
}
