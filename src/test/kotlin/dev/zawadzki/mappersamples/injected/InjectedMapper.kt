package dev.zawadzki.mappersamples.injected

import io.mockk.every
import io.mockk.mockk
import javax.inject.Inject
import kotlin.test.Test
import kotlin.test.assertEquals

typealias SampleApi = () -> SampleDTO

class InjectedMapper @Inject constructor() {

    fun map(dto: SampleDTO): SampleDomainModel = SampleDomainModel(
        property1 = dto.property1, property2 = dto.property2
    )

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
    private val sampleMapper: InjectedMapper,
    private val sampleApi: SampleApi
) {

    fun sampleGet(): SampleDomainModel {
        val dto = sampleApi.invoke()
        return sampleMapper.map(dto)
    }

}

class SampleRepositoryWithInjectedMapperTest {

    @Test
    fun `should return domain model from repository`() {
        val sampleDto = SampleDTO("xxx", 123L)
        val sampleDomainModel = SampleDomainModel("MOCKED xxx", 123L)
        val mockMapper = mockk<InjectedMapper> {
            every { map(sampleDto) } returns sampleDomainModel
        }
        val repository = SampleRepository(sampleMapper = mockMapper, sampleApi = { sampleDto })

        val result = repository.sampleGet()

        assertEquals(sampleDomainModel, result)
    }
}