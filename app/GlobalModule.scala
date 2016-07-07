
import com.despegar.indec.hangar.actions.{RouterRequest, RouterRequestActionBuilder}
import com.despegar.indec.hangar.controllers.ExampleController
import com.despegar.indec.hangar.services.ExampleService
import com.despegar.vr.commons.actions.ActionBuilder
import scaldi.{Module => ScaldiModule}


class GlobalModule extends ScaldiModule {

  // Serializer
    bind[JsonSerializer] to Json4sSnakeCaseSerializer(Nil)

  // Action Builder
    bind[ActionBuilder[RouterRequest]] toNonLazy new RouterRequestActionBuilder()

  // Controllers
    binding toNonLazy new ExampleController()

  // Services
    binding toNonLazy new ExampleService()


}
