package dev.zawadzki.mappersamples.constructorfunction

import javax.inject.Inject
import kotlin.test.Test
import kotlin.test.assertEquals

typealias SampleApi = () -> SampleDTO

data class SampleDTO(
    val property1: String?,
    val property2: Long
)

data class SampleDomainModel(
    val property1: String?,
    val property2: Long
)

@Suppress("TestFunctionName")
fun SampleDomainModel(dto: SampleDTO): SampleDomainModel = SampleDomainModel(
    property1 = dto.property1, property2 = dto.property2
)

class SampleRepository @Inject constructor(
    private val sampleApi: SampleApi
) {

    fun sampleGet(): SampleDomainModel {
        val dto = sampleApi.invoke()
        return SampleDomainModel(dto)
    }

}

class SampleRepositoryWithConstructorFunctionMapperTest {

    @Test
    fun `should return domain model from repository`() {
        // no stubbing, real mapper gets called
        val sampleDto = SampleDTO("xxx", 123L)
        val sampleDomainModel = SampleDomainModel("xxx", 123L)

        val repository = SampleRepository(sampleApi = { sampleDto })

        val result = repository.sampleGet()

        assertEquals(sampleDomainModel, result)
    }
}
