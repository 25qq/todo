package com.example.todo.data.utils;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;

@ToString
@EqualsAndHashCode
public class OffsetLimitRequest implements Pageable, Serializable {

    private static final long serialVersionUID = -25822477129613575L;

    private int limit;
    private long offset;
    private final Sort sort;


    public OffsetLimitRequest(long offset, int limit, Sort sort) {
        if (offset < 0) {
            throw new IllegalArgumentException("Offset index must not be less than zero!");
        }

        if (limit < 1) {
            throw new IllegalArgumentException("Limit must not be less than one!");
        }
        this.limit = limit;
        this.offset = offset;
        this.sort = sort;
    }


    public OffsetLimitRequest(long offset, int limit, Sort.Direction direction, String... properties) {
        this(offset, limit, Sort.by(direction, properties));
    }

    public OffsetLimitRequest(long offset, int limit) {
        this(offset, limit, Sort.by(Sort.Direction.ASC, "id"));
    }

    @Override
    public int getPageNumber() {
        return (int) (offset / limit);
    }

    @Override
    public int getPageSize() {
        return limit;
    }

    @Override
    public long getOffset() {
        return offset;
    }

    @Override
    public Sort getSort() {
        return sort;
    }

    @Override
    public Pageable next() {
        return new OffsetLimitRequest(getOffset() + getPageSize(), getPageSize(), getSort());
    }

    public OffsetLimitRequest previous() {
        return hasPrevious() ? new OffsetLimitRequest(getOffset() - getPageSize(), getPageSize(), getSort()) : this;
    }


    @Override
    public Pageable previousOrFirst() {
        return hasPrevious() ? previous() : first();
    }

    @Override
    public Pageable first() {
        return new OffsetLimitRequest(0, getPageSize(), getSort());
    }

    @Override
    public boolean hasPrevious() {
        return offset > limit;
    }

    @Override
    public Pageable withPage(int pageNumber) {
        return null;
    }





}