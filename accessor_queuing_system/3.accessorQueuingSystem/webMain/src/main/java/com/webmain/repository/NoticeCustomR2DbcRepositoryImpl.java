package com.webmain.repository;

import com.webmain.domain.entity.Notice;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.ZonedDateTime;

@Repository
@RequiredArgsConstructor
public class NoticeCustomR2DbcRepositoryImpl implements NoticeCustomR2dbcRepository {

    private final DatabaseClient databaseClient;

    @Override
    public Flux<Notice> findAll() {
        var sql = """
                SELECT n.no as no, n.title, n.content, n.created_at as createdAt, n.updated_at as updatedAt
                FROM notice n
                """;
        return databaseClient.sql(sql)
                .fetch()
                .all()
                .map(row -> Notice.builder()
                        .no((Long) row.get("no"))
                        .title((String) row.get("title"))
                        .content((String) row.get("content"))
                        .createdAt(((ZonedDateTime) row.get("createdAt")).toLocalDateTime())
                        .updatedAt(((ZonedDateTime) row.get("updatedAt")).toLocalDateTime())
                        .build()
                );
    }
}
