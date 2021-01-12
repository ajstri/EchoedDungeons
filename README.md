[license]: https://img.shields.io/badge/License-Apache%202.0-lightgrey.svg
[issues]: https://img.shields.io/github/issues/ajstri/EchoedCore.svg 
[issues-link]: https://github.com/ajstri/EchoedDungeons/issues
[ ![license][] ](https://github.com/ajstri/EchoedDungeons/blob/master/LICENSE)
[ ![issues][] ][issues-link]

# EchoedDungeons - Dungeons & Dragons Utility Discord bot 
#### A simple-to-use Discord Bot written in Java

# Features
#### Rolling dice

> `[required] (optional)`


> `[prefix]roll stats`

> `[prefix]roll <dice>`

> `[prefix]roll (s)[AmountOfDice]d[SidesOfDice]([+|-][Modifier])(dl|kl|kh|dh)`


> `stats` - automatically rolls standard stats (4d6dl, 6 times)

> `s` - include if you want the roll negative

> `([+|-][Modifier])` - include full part if you need modifiers. Example: +5

> `(dl|kl|kh|dh)` - DropLowest, KeepLowest, KeepHighest, DropHighest
--------------------
#### Calling a die roll (DMs only)

> `[required] (optional)`


> `ed!call [arg] @Target`

> [arg] can be `initiative`, `attack`, or a `skill` (any).

> `@Target` must be a tag in order to work correctly.
--------------------
#### Database

> `[required] (optional)`


> `[prefix]class (ClassName)` - Returns information on `(ClassName)`. If none, returns a list of supported classes

> `[prefix]feature (ClassName|SubclassName) (FeatureName)` - Returns information on `(FeatureName)` based on the provided `(ClassName)`. If none, returns usage instructions. If just `(ClassName)`, returns list of features from that class. Feature can be from `SubclassName`. It will search both the class and the subclass when searching for features, so you can use the root `ClassName` when looking for a subclass feature.

> `[prefix]background (BackgroundName)` - **[NOT YET IMPLEMENTED]** Returns information on `(BackgroundName)`. If none, returns a list of supported backgrounds

> `[prefix]race (RaceName)` - **[NOT YET IMPLEMENTED]** Returns information on `(RaceName)`. If none, returns a list of supported races

> `[prefix]fact` - Returns a random D&D fact
--------------------
#### Evaluating mathematical expressions

> `[required] (optional)`


> `[prefix]m [Expression]`

> `[prefix]m factorial [Number]`

> `[prefix]m mode [degrees|radians]`

List of supported math functions


Addition, subtraction

Multiplication, division

Sine (inverse, hyperbolic, inverse hyperbolic (sin, asin, sinh, asinh))

Cosine (inverse, hyperbolic, inverse hyperbolic (cos, acos, cosh, acosh))

Tangent (inverse, hyperbolic, inverse hyperbolic (tan, atan, tanh, atanh))

Logarithmic and natural log

Exponents

# How to use the bot

[WIP]
