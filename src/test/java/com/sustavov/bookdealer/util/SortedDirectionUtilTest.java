package com.sustavov.bookdealer.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class SortedDirectionUtilTest {

    @Test
    void getOrders() {
        try (MockedStatic<SortedDirectionUtil> utilities = Mockito.mockStatic(SortedDirectionUtil.class)) {
            utilities.when(() -> SortedDirectionUtil.getOrders(new String[]{"id", "asc"})).thenReturn(List.of());

            String[] sort = new String[]{"id", "asc"};
            assertThat(SortedDirectionUtil.getOrders(sort)).isEqualTo(List.of());
        }
    }
}