version 1.0

## Import Struct and add them to the global nmamespac
import "simple.wdl" alias Foo as ExtendedFamily

struct Team {
    Array[Person] teams
}