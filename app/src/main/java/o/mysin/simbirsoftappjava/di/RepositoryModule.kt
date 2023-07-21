package o.mysin.simbirsoftappjava.di

import o.mysin.simbirsoftappjava.domain.repository.HelpCategoryRepository
import o.mysin.simbirsoftappjava.data.repository.HelpCategoryRepositoryImpl
import o.mysin.simbirsoftappjava.domain.repository.NewsRepository
import o.mysin.simbirsoftappjava.data.repository.NewsRepositoryImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {

    single<HelpCategoryRepository> {
        HelpCategoryRepositoryImpl(get(), get(named("helpCategories")))
    }

    single<NewsRepository> {
        NewsRepositoryImpl(get(), get(named("news")), get(named("newsFake")))
    }

}
