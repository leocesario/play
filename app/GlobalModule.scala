
import com.despegar.indec.hangar.actions.{HangarRequest, HangarRequestActionBuilder}
import com.despegar.indec.hangar.controllers.ExampleController
import com.despegar.indec.hangar.services.ExampleService
import com.despegar.vr.commons.actions.ActionBuilder
import com.despegar.vr.commons.serialization.json.{Json4sSnakeCaseSerializer, JsonSerializer}
import scaldi.{Module => ScaldiModule}


class GlobalModule extends ScaldiModule {

  // Serializer
    bind[JsonSerializer] to new Json4sSnakeCaseSerializer(Nil)

  // Action Builder
    bind[ActionBuilder[HangarRequest]] toNonLazy new HangarRequestActionBuilder()

  // Controllers
    binding toNonLazy new ExampleController()

  // Services
    binding toNonLazy new ExampleService()


}
