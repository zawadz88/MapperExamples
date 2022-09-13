package dev.zawadzki.mappersamples.fileextensionfunction

import io.mockk.every
import io.mockk.mockkStatic
import javax.inject.Inject
import kotlin.test.Test
import kotlin.test.assertEquals

typealias SampleApi = () -> SampleDTO

fun SampleDTO.map(): SampleDomainModel = SampleDomainModel(
    property1 = this.property1, property2 = this.property2
)

data class SampleDTO(
    val property1: String?,
    val property2: Long
)

data class SampleDomainModel(
    val property1: String?,
    val property2: Long
)

class SampleRepository @Inject constructor(
    private val sampleApi: SampleApi
) {

    fun sampleGet(): SampleDomainModel {
        val dto = sampleApi.invoke()
        return dto.map()
    }

}

class SampleRepositoryWithFileExtensionFunctionMapperTest {

    @Test
    fun `should return domain model from repository`() = mockkStatic(SampleDTO::map) {
        val sampleDto = SampleDTO("xxx", 123L)
        val sampleDomainModel = SampleDomainModel("MOCKED xxx", 123L)
        every { sampleDto.map() } returns sampleDomainModel
        val repository = SampleRepository(sampleApi = { sampleDto })

        val result = repository.sampleGet()

        assertEquals(sampleDomainModel, result)
    }
}