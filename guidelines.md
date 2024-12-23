**TODO** finish
**TODO* ADD EXAMPLES FOR EVERYTHING (IN CODE FORM, FROM ACTUAL CODE)

### Naming

#### Naming Forms

##### _PascalCase_

##### _camalCase_

#### Variables

- Names should follow _camalCase_ form.
- Names should signify what the variable is storing.
    - Example: For a variable storing a sum of numbers - use the name `sum`
    - Example: For a variable storing an angle - use the name `angle`
- Names should be at least 3 letters long.
    - Shorter names are unclear and typically lack real meaning.
    - Example: `n` is a bad name
    - Special Case: using `i`/`j`/`k` for index storing in `for` loop is acceptable.
- Names should typically be nouns, as they store stuff.
- Avoid ambiguous names - names that are not clear as to what they are refering
    - This is context based, as in different situations, names will have different meanings
    - Example: the name `size` in the class `Building` is unclear - "size of what?". Use `height` instead (for instance).
    - Long but clear names are preferable over short but ambiguous
- When a variable holds data using a specific measurement unit, specify what it is in the name
    - Example: `distanceMeters`, `angleDegrees`

#### Methods

- Names should follow _camalCase_ form.
- Names should signify what the method is doing
- Names should typically be constructed out of verbs
    - Methods do stuff, the name indicates what
- Parameter names should follow the rules for variable names
- When the method returns data using a specific measurement unit, specify what it is in the name
    - Example: `getDistanceMeters`, `getHeightCentimeters`

#### Classes


#### Packages

### Command-Based

- Device instances/classes should only be held inside the subsystem that owns them. Do not expose or share the instance with other subsystems or command.
    - If a command wishes to access information from that devices or use it, the subsystem should expose a method that retreives that information or receives commands to the device.
