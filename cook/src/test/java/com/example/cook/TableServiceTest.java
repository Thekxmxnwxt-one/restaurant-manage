package com.example.cook;

import com.example.cook.enums.TableStatus;
import com.example.cook.exception.NotFoundException;
import com.example.cook.model.TablesModel;
import com.example.cook.repository.TableNativeRepository;
import com.example.cook.service.TableService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TableServiceTest {

    @Mock
    private TableNativeRepository tableNativeRepository;

    @InjectMocks
    private TableService tableService;

    @Test
    public void test_get_all_table_expect_success() {
        when(tableNativeRepository.findAllTable()).thenReturn(List.of(new TablesModel()));

        List<TablesModel> result = tableService.getAllTable();

        Assertions.assertTrue(result.size()>0);
        verify(tableNativeRepository).findAllTable();
    }

    @Test
    public void test_get_all_table_expect_runtime_exception() {
        when(tableNativeRepository.findAllTable()).thenThrow(new RuntimeException("DB connection failed"));

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            tableService.getAllTable();
        });

        Assertions.assertTrue(exception.getMessage().contains("Failed to get all tables"));
        verify(tableNativeRepository).findAllTable();
    }

    @Test
    public void test_get_table_by_number_expect_success() {

        TablesModel mockTable = new TablesModel();
        mockTable.setTableNumber(1);

        when(tableNativeRepository.findTableByTableNumber(1)).thenReturn(mockTable);

        TablesModel result = tableService.getTableByTableNumber(1);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.getTableNumber());
        verify(tableNativeRepository).findTableByTableNumber(1);
    }

    @Test
    public void test_get_table_by_number_expect_not_found() {

        when(tableNativeRepository.findTableByTableNumber(1)).thenReturn(null);

        NotFoundException ex = Assertions.assertThrows(NotFoundException.class,
                () -> tableService.getTableByTableNumber(1));

        Assertions.assertTrue(ex.getMessage().contains("Table number " + 1 + " not found"));
        verify(tableNativeRepository).findTableByTableNumber(1);
    }

    @Test
    public void test_get_table_by_number_expect_runtime_exception() {

        when(tableNativeRepository.findTableByTableNumber(1)).thenThrow(new RuntimeException("DB connection failed"));

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            tableService.getTableByTableNumber(1);
        });

        Assertions.assertTrue(exception.getMessage().contains("Failed to get table by number"));
        verify(tableNativeRepository).findTableByTableNumber(1);
    }

    @Test
    public void test_insert_table_expect_success() {

        List<TablesModel> tablesModels = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            TablesModel x = new TablesModel();
            x.setTableNumber(i);
            x.setStatus(TableStatus.AVAILABLE);
            tablesModels.add(x);
        }

        when(tableNativeRepository.insertTable(tablesModels)).thenReturn(2);

        int result = tableService.insertTable(tablesModels);

        Assertions.assertEquals(2, result);
        verify(tableNativeRepository).insertTable(tablesModels);
    }

    @Test
    public void test_insert_table_expect_runtime_exception() {
        List<TablesModel> tablesModels = new ArrayList<>();
        TablesModel table = new TablesModel();
        table.setTableNumber(1);
        table.setStatus(TableStatus.AVAILABLE);
        tablesModels.add(table);

        when(tableNativeRepository.insertTable(tablesModels))
                .thenThrow(new RuntimeException("DB connection failed"));

        RuntimeException ex = Assertions.assertThrows(RuntimeException.class, () -> {
            tableService.insertTable(tablesModels);
        });

        Assertions.assertTrue(ex.getMessage().contains("Failed to insert tables"));
        verify(tableNativeRepository).insertTable(tablesModels);
    }

    @Test
    public void test_update_status_table_expect_success(){

        TablesModel oldStatusTable = new TablesModel();
        oldStatusTable.setTableNumber(1);
        oldStatusTable.setStatus(TableStatus.AVAILABLE);

        TablesModel updateTable = new TablesModel();
        updateTable.setTableNumber(1);
        updateTable.setStatus(TableStatus.NOT_AVAILABLE);

        when(tableNativeRepository.updateTable(oldStatusTable)).thenReturn(updateTable);

        TablesModel result = tableService.updateTable(oldStatusTable);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.getTableNumber());
        Assertions.assertEquals(TableStatus.NOT_AVAILABLE, result.getStatus());
        verify(tableNativeRepository).updateTable(oldStatusTable);
    }

    @Test
    public void test_update_status_table_expect_not_found(){

        TablesModel tablesModel = new TablesModel();
        tablesModel.setTableNumber(1);
        tablesModel.setStatus(TableStatus.AVAILABLE);

        when(tableNativeRepository.updateTable(tablesModel)).thenReturn(null);

        NotFoundException ex = Assertions.assertThrows(NotFoundException.class,
                () -> tableService.updateTable(tablesModel));

        Assertions.assertTrue(ex.getMessage().contains("Table not found or update failed."));
        verify(tableNativeRepository).updateTable(tablesModel);
    }

    @Test
    public void test_update_status_table_expect_runtime_exception(){

        TablesModel tablesModel = new TablesModel();
        tablesModel.setTableNumber(1);
        tablesModel.setStatus(TableStatus.AVAILABLE);

        when(tableNativeRepository.updateTable(tablesModel))
                .thenThrow(new RuntimeException("DB connection failed"));

        RuntimeException ex = Assertions.assertThrows(RuntimeException.class, () -> {
            tableService.updateTable(tablesModel);
        });

        Assertions.assertTrue(ex.getMessage().contains("Failed to update table:"));
        verify(tableNativeRepository).updateTable(tablesModel);
    }

    @Test
    public void test_delete_table_by_id_expect_success() {
        int tableId = 1;

        when(tableNativeRepository.deleteTable(tableId)).thenReturn("Delete successful");

        String result = tableService.deleteTable(tableId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Delete successful", result);
        verify(tableNativeRepository).deleteTable(tableId);
    }

    @Test
    public void test_delete_table_by_id_expect_runtime_exception() {
        int tableId = 1;

        when(tableNativeRepository.deleteTable(tableId)).thenThrow(new RuntimeException("DB connection failed"));

        RuntimeException ex = Assertions.assertThrows(RuntimeException.class, () -> {
            tableService.deleteTable(tableId);
        });

        Assertions.assertTrue(ex.getMessage().contains("Failed to delete table with id " + tableId + ": "));
        verify(tableNativeRepository).deleteTable(tableId);

    }
}
