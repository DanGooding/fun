# fun
My little functional programming language. 

Current features include 
- lazy evaluation
- pattern matching
- recursion (and mutual recursion)
- lists, tuples, booleans, and arbitrary size integers
- no side effects, just expressions that evaluate to a values
- Hindley-Milner type inference

Programs are interpreted as follows: parse, infer & check types, evaluate

## An example program (the fibonacci sequence)
```
let add = lambda x -> lambda y -> x + y
in let tail = lambda (_ : xs) -> xs

# combines two lists into one by applying a function pairwise
in let zipWith = lambda f -> lambda xs -> lambda ys ->
    case (xs, ys) of
        (x : xs', y : ys') -> f x y : zipWith f xs' ys'
        _ -> []

# takes the first n elements of the given (possibly infinite) list
in let take = lambda n -> lambda xs ->
    if n == 0
        then []
        else case xs of
            [] -> []
            x : xs' -> x : take (n - 1) xs'

# an infinite list of the fibonacci numbers!
in let fibs = 0 : 1 : zipWith add fibs (tail fibs)

# laziness means only the first 15 are actually computed here
in take 15 fibs
```
When run this gives the result's type and value. (list printed with cons notation)
```
[Integer]
(0 : (1 : (1 : (2 : (3 : (5 : (8 : (13 : (21 : (34 : (55 : (89 : (144 : (233 : (377 : [])))))))))))))))
```

