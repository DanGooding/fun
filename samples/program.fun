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