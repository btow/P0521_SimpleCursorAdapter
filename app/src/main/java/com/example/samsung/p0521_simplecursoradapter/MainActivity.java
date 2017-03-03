package com.example.samsung.p0521_simplecursoradapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends AppCompatActivity {

    private static final int CM_DELETE_ID = 1;
    ListView lvData;
    DB db;
    SimpleCursorAdapter scAdapter;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //Открываем подключение к БД
        db = new DB(this);
        db.openDb();
        //Получаем курсор в таблице БД
        cursor = db.getAllData();
        startManagingCursor(cursor);
        //Массив наименований колонок, которые будут сопоставляться ID View-компонентов
        String[] from = {DB.COLUMN_IMG, DB.COLUMN_TXT};
        //Массив ID View-компонентов, в которые будут записваться данные из столбцов
        int[] to = {R.id.ivImg, R.id.tvText};
        //Создание адептера и сопоставление его списку
        scAdapter = new SimpleCursorAdapter(this, R.layout.item, cursor, from, to);
        lvData = (ListView) findViewById(R.id.lvData);
        lvData.setAdapter(scAdapter);
        //Прикрепляем к списку контекстное меню
        registerForContextMenu(lvData);
    }

    //Обработчик собатия "нажатие кнопки btnAdd
    public  void onButtonClic(View view) {
        //добавление записи
        db.addRec("Sometext " + (cursor.getCount() + 1), R.mipmap.ic_launcher );
        //Обновление курсора в таблице
        cursor.requery();
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view,
                                    ContextMenu.ContextMenuInfo contextMenuInfo) {
        super.onCreateContextMenu(contextMenu, view, contextMenuInfo);
        contextMenu.add(0, CM_DELETE_ID, 0, R.string.record_del);
    }

    @Override
    public boolean onContextItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == CM_DELETE_ID) {
            //Извлекаем из пункта контекстного меню данные по пункту списка
            AdapterView.AdapterContextMenuInfo adapterContextMenuInfo
                    = (AdapterView.AdapterContextMenuInfo) menuItem.getMenuInfo();
            //Извлекаем ID записи и удаляем соответствующую запись из БД
            db.delRec(adapterContextMenuInfo.id);
            //Обновление курсора
            cursor.requery();
            return true;
        }
        return super.onContextItemSelected(menuItem);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Закрытие подключения к БД при выходе
        db.closeDb();
    }
}
