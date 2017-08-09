import com.google.inject.AbstractModule
import models.graphql.{InterfaceTypeDefinitions, TypeDefinitions}

class Module extends AbstractModule {

  override def configure(): Unit = {
    bind(classOf[InterfaceTypeDefinitions]).asEagerSingleton()
    bind(classOf[TypeDefinitions]).asEagerSingleton()
  }
}
