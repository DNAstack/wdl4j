version 1.0

## Import Struct and add them to the global nmamespac
import "simple.wdl"
struct Family {
    Array[Person] members
}

struct Team {
    Array[Person] teams
}