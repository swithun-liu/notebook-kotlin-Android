// FIR_COMPARISON
fun some() {

    if (true) {
        <caret>
    } else {

    }

}


// EXIST: do
// EXIST: class
// EXIST: false
// EXIST: for
// EXIST: fun
// EXIST: if
// EXIST: interface
// EXIST: null
// EXIST: object
// EXIST: return
// EXIST: throw
// EXIST: true
// EXIST: try
// EXIST: typealias
// EXIST: val
// EXIST: var
// EXIST: when
// EXIST: while

// NOTHING_ELSE