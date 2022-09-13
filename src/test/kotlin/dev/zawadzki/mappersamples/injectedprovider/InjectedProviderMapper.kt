package dev.zawadzki.mappersamples.injectedprovider

import io.mockk.every
import io.mockk.mockk
import javax.inject.Inject
import kotlin.test.Test
import kotlin.test.assertEquals

typealias SampleApi = () -> SampleDTO

object SampleModule {

    // @Provides
    fun provideMapper(): (SampleDTO) -> SampleDomainModel = { dto ->
        SampleDomainModel(
            property1 = dto.property1, property2 = dto.property2
        )
    }

}

data class SampleDTO(
    val property1: String?,
    val property2: Long
)

data class SampleDomainModel(
    val property1: String?,
    val property2: Long
)

class SampleRepository @Inject constructor(
    private val sampleMapper: (SampleDTO) -> SampleDomainModel,
    private val sampleApi: SampleApi
) {

    fun sampleGet(): SampleDomainModel {
        val dto = sampleApi.invoke()
        return sampleMapper(dto)
    }

}

class SampleRepositoryWithInjectedProviderMapperTest {

    @Test
    fun `should return domain model from repository`() {
        val sampleDto = SampleDTO("xxx", 123L)
        val sampleDomainModel = SampleDomainModel("MOCKED xxx", 123L)
        val mockMapper = mockk<(SampleDTO) -> SampleDomainModel> {
            every { this@mockk.invoke(sampleDto) } returns sampleDomainModel
        }
        val repository = SampleRepository(sampleMapper = mockMapper, sampleApi = { sampleDto })

        val result = repository.sampleGet()

        assertEquals(sampleDomainModel, result)
    }
}