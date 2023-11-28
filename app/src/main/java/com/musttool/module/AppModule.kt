package com.musttool.module

import android.app.Activity
import android.content.Context
import androidx.room.Room
import com.musttool.MustToolDatabase
import com.musttool.Notes.NotesDAO
import com.musttool.data.NotesApp.NotesAppRepo
import com.musttool.data.NotesApp.NotesAppRepoImpl
import com.musttool.data.QrCodeGenerate.qrcodegeneraterrepo.GenerateQRCodeRepo
import com.musttool.data.QrCodeGenerate.qrcodegeneraterrepo.QrCodeGeneratorImplRepo
import com.musttool.data.TextExtractor.TextExtractorRepo.TextExtractorRepo
import com.musttool.data.TextExtractor.TextExtractorRepo.TextExtractorRepoImpl
import com.musttool.module.password.openHelperFactory
import com.musttool.utils.CameraPermissionHandler.CameraPermissionHandler
import com.musttool.utils.CameraPermissionHandler.InterCameraPermissionHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun getQrCodeGenerator():GenerateQRCodeRepo  = QrCodeGeneratorImplRepo()

    @Provides
    @Singleton
    fun getNotesAppRepo(mustToolDatabase: MustToolDatabase):NotesAppRepo  = NotesAppRepoImpl(mustToolDatabase)

    @Provides
    @Singleton
    fun provideMustToolDatabase(@ApplicationContext context: Context): MustToolDatabase {
        return Room.databaseBuilder(context, MustToolDatabase::class.java, "flashcash_database").openHelperFactory(openHelperFactory).build()
    }

    @Provides
    @Singleton
    fun provideNoteDao(database: MustToolDatabase): NotesDAO {
        return database.noteDao()
    }

    @Provides
    @Singleton
    fun getCameraPermissionHandlerObject(@ActivityContext context: Activity): InterCameraPermissionHandler {
        return CameraPermissionHandler(context)
    }

    @Provides
    @Singleton
    fun getTextExtractorRepoObj():TextExtractorRepo{
        return TextExtractorRepoImpl()
    }

}
object password { // lock on data base.
    val openHelperFactory = SupportFactory(SQLiteDatabase.getBytes("!@@##$%%QWEERRTT/.,><~_++".toCharArray()))
}