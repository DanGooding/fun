package fun.types;

/**
 * either a monotype or polytype
 */
public interface TypeLike extends Substitutable<TypeLike> {

    public abstract Type instantiate(Inferer inferer);

    abstract TypeLike refreshVariableNames(VariableNameRefresher v);

}
