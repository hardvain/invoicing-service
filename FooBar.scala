case class FooBar(number: Int)
def calculateFooBar(optionalFooBar: Option[FooBar]): String = {
  optionalFooBar match {
    case Some(FooBar(number)) =>
      (number % 3, number % 5) match {
        case (0, 0) => "foobar"
        case (0, _) => "foo"
        case (_, 0) => "bar"
        case _ => number.toString
      }
    case None => "Cannot calculate foo bar"
  }
}

assert(calculateFooBar(Some(FooBar(15))) == "foobar")
assert(calculateFooBar(Some(FooBar(12))) == "foo")
assert(calculateFooBar(Some(FooBar(10))) == "bar")
assert(calculateFooBar(Some(FooBar(1))) == "1")
assert(calculateFooBar(None) == "Cannot calculate foo bar")