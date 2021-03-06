/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.error.OptionalShouldBeEmpty.shouldBeEmpty;
import static org.assertj.core.error.OptionalShouldBePresent.shouldBePresent;
import static org.assertj.core.error.OptionalShouldContain.shouldContain;
import static org.assertj.core.error.OptionalShouldContain.shouldContainSame;
import static org.assertj.core.error.OptionalShouldContainInstanceOf.shouldContainInstanceOf;

import java.util.Comparator;
import java.util.Optional;

import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.ComparisonStrategy;
import org.assertj.core.internal.FieldByFieldComparator;
import org.assertj.core.internal.StandardComparisonStrategy;

/**
 * Assertions for {@link java.util.Optional}.
 *
 * @param <T> type of the value contained in the {@link java.util.Optional}.
 * @author Jean-Christophe Gay
 * @author Nicolai Parlog
 */
public abstract class AbstractOptionalAssert<S extends AbstractOptionalAssert<S, T>, T> extends
    AbstractAssert<S, Optional<T>> {

  private ComparisonStrategy optionalValueComparisonStrategy;

  protected AbstractOptionalAssert(Optional<T> actual, Class<?> selfType) {
    super(actual, selfType);
    this.optionalValueComparisonStrategy = StandardComparisonStrategy.instance();
  }

  /**
   * Verifies that there is a value present in the actual {@link java.util.Optional}.
   * </p>
   * Assertion will pass :
   * <pre><code class='java'> assertThat(Optional.of("something")).isPresent();</code></pre>
   * 
   * Assertion will fail :
   * <pre><code class='java'> assertThat(Optional.empty()).isPresent();</code></pre>
   *
   * @return this assertion object.
   */
  public S isPresent() {
    isNotNull();
    if (!actual.isPresent()) throwAssertionError(shouldBePresent(actual));
    return myself;
  }

  /**
   * Verifies that the actual {@link java.util.Optional} is empty.
   * </p>
   * Assertion will pass :
   * <pre><code class='java'> assertThat(Optional.empty()).isEmpty();</code></pre>
   * 
   * Assertion will fail :
   * <pre><code class='java'> assertThat(Optional.of("something")).isEmpty();</code></pre>
   *
   * @return this assertion object.
   */
  public S isEmpty() {
    isNotNull();
    if (actual.isPresent()) throwAssertionError(shouldBeEmpty(actual));
    return myself;
  }

  /**
   * Verifies that the actual {@link java.util.Optional} contains the given value (alias of {@link #hasValue(Object)}).
   * </p>
   * Assertion will pass :
   * <pre><code class='java'> assertThat(Optional.of("something")).contains("something");
   * assertThat(Optional.of(10)).contains(10);</code></pre>
   * 
   * Assertion will fail :
   * <pre><code class='java'> assertThat(Optional.of("something")).contains("something else");
   * assertThat(Optional.of(20)).contains(10);</code></pre>
   *
   * @param expectedValue the expected value inside the {@link java.util.Optional}.
   * @return this assertion object.
   */
  public S contains(T expectedValue) {
    isNotNull();
    checkNotNull(expectedValue);
    if (!actual.isPresent()) throwAssertionError(shouldContain(expectedValue));
    if (!optionalValueComparisonStrategy.areEqual(actual.get(), expectedValue)) throwAssertionError(shouldContain(actual, expectedValue));
    return myself;
  }

  /**
   * Verifies that the actual {@link java.util.Optional} contains the given value (alias of {@link #contains(Object)}).
   * </p>
   * Assertion will pass :
   * <pre><code class='java'> assertThat(Optional.of("something")).hasValue("something");
   * assertThat(Optional.of(10)).contains(10);</code></pre>
   * 
   * Assertion will fail :
   * <pre><code class='java'> assertThat(Optional.of("something")).hasValue("something else");
   * assertThat(Optional.of(20)).contains(10);</code></pre>
   *
   * @param expectedValue the expected value inside the {@link java.util.Optional}.
   * @return this assertion object.
   */
  public S hasValue(T expectedValue) {
    return contains(expectedValue);
  }

  /**
   * Verifies that the actual {@link Optional} contains a value that is an instance of the argument.
   * </p>
   * Assertions will pass:
   *
   * <pre><code class='java'> assertThat(Optional.of("something")).containsInstanceOf(String.class)
   *                                     .containsInstanceOf(Object.class);
   *                                     
   * assertThat(Optional.of(10)).containsInstanceOf(Integer.class);</code></pre>
   *
   * Assertion will fail:
   *
   * <pre><code class='java'> assertThat(Optional.of("something")).containsInstanceOf(Integer.class);</code></pre>
   *
   * @param clazz the expected class of the value inside the {@link Optional}.
   * @return this assertion object.
   */
  public S containsInstanceOf(Class<?> clazz) {
    isNotNull();
    if (!actual.isPresent()) throwAssertionError(shouldBePresent(actual));
    if (!clazz.isInstance(actual.get())) throwAssertionError(shouldContainInstanceOf(actual, clazz));
    return myself;
  }

  /**
   * Use field/property by field/property comparison (including inherited fields/properties) instead of relying on
   * actual type A <code>equals</code> method to compare the {@link Optional} value's object for incoming assertion
   * checks. Private fields are included but this can be disabled using
   * {@link Assertions#setAllowExtractingPrivateFields(boolean)}.
   * <p>
   * This can be handy if <code>equals</code> method of the {@link Optional} value's object to compare does not suit
   * you.
   * </p>
   * Note that the comparison is <b>not</b> recursive, if one of the fields/properties is an Object, it will be
   * compared to the other field/property using its <code>equals</code> method.
   * <p/>
   * Example:
   * 
   * <pre><code class='java'> TolkienCharacter frodo = new TolkienCharacter("Frodo", 33, HOBBIT);
   * TolkienCharacter frodoClone = new TolkienCharacter("Frodo", 33, HOBBIT);
   *  
   * // Fail if equals has not been overridden in TolkienCharacter as equals default implementation only compares references
   * assertThat(Optional.of(frodo)).contains(frodoClone);
   *  
   * // frodo and frodoClone are equals when doing a field by field comparison.
   * assertThat(Optional.of(frodo)).usingFieldByFieldValueComparator().contains(frodoClone);</code></pre>
   *
   * @return {@code this} assertion object.
   */
  public S usingFieldByFieldValueComparator() {
    return usingValueComparator(new FieldByFieldComparator());
  }

  /**
   * Use given custom comparator instead of relying on actual type A <code>equals</code> method to compare the
   * {@link Optional} value's object for incoming assertion checks.
   * <p>
   * Custom comparator is bound to assertion instance, meaning that if a new assertion is created, it will use default
   * comparison strategy.
   * <p>
   * Examples :
   *
   * <pre><code class='java'> TolkienCharacter frodo = new TolkienCharacter("Frodo", 33, HOBBIT);
   * TolkienCharacter frodoClone = new TolkienCharacter("Frodo", 33, HOBBIT);
   * 
   * // Fail if equals has not been overridden in TolkienCharacter as equals default implementation only compares references
   * assertThat(Optional.of(frodo)).contains(frodoClone);
   * 
   * // frodo and frodoClone are equals when doing a field by field comparison.
   * assertThat(Optional.of(frodo)).usingValueComparator(new FieldByFieldComparator()).contains(frodoClone);</code></pre>
   *
   * @param customComparator the comparator to use for incoming assertion checks.
   * @throws NullPointerException if the given comparator is {@code null}.
   * @return {@code this} assertion object.
   */
  public S usingValueComparator(Comparator<? super T> customComparator) {
    optionalValueComparisonStrategy = new ComparatorBasedComparisonStrategy(customComparator);
    return myself;
  }

  /**
   * Revert to standard comparison for incoming assertion {@link Optional} value checks.
   * <p>
   * This method should be used to disable a custom comparison strategy set by calling
   * {@link #usingValueComparator(Comparator)}.
   *
   * @return {@code this} assertion object.
   */
  public S usingDefaultValueComparator() {
    // fall back to default strategy to compare actual with other objects.
    optionalValueComparisonStrategy = StandardComparisonStrategy.instance();
    return myself;
  }

  /**
   * Verifies that the actual {@link java.util.Optional} contains the instance given as an argument (i.e. it must be the
   * same instance).
   * </p>
   * Assertion will pass :
   * 
   * <pre><code class='java'> String someString = "something";
   * assertThat(Optional.of(someString)).containsSame(someString);
   * 
   * // Java will create the same 'Integer' instance when boxing small ints
   * assertThat(Optional.of(10)).containsSame(10);</code></pre>
   * 
   * Assertion will fail :
   * 
   * <pre><code class='java'> // not even equal:
   * assertThat(Optional.of("something")).containsSame("something else");
   * assertThat(Optional.of(20)).containsSame(10);
   * 
   * // equal but not the same: 
   * assertThat(Optional.of(new String("something"))).containsSame(new String("something"));
   * assertThat(Optional.of(new Integer(10))).containsSame(new Integer(10));</code></pre>
   *
   * @param expectedValue the expected value inside the {@link java.util.Optional}.
   * @return this assertion object.
   */
  public S containsSame(T expectedValue) {
    isNotNull();
    checkNotNull(expectedValue);
    if (!actual.isPresent()) throwAssertionError(shouldContain(expectedValue));
    if (actual.get() != expectedValue) throwAssertionError(shouldContainSame(actual, expectedValue));
    return myself;
  }

  private void checkNotNull(T expectedValue) {
    if (expectedValue == null) throw new IllegalArgumentException("The expected value should not be <null>.");
  }

}
