package com.epam.esm.repository;

import java.util.List;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.SearchGiftCertificateParameter;

public interface GiftCertificateRepository extends CrdOperations<GiftCertificate> {

    public List<GiftCertificate> findAll(SearchGiftCertificateParameter parametr, int offset, int limit);

    public GiftCertificate update(GiftCertificate giftCertificate);

    public long getCountOfEntities(SearchGiftCertificateParameter parameter);

}
