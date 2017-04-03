package org.egov.pgr.employee.enrichment.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class PositionTest {

    @Test
    public void test_should_return_true_when_both_position_instances_are_equal() {
        final Position position1 = new Position("1", "2");
        final Position position2 = new Position("1", "2");

        assertTrue(position1.equals(position2));
    }

    @Test
    public void test_should_return_same_hash_code_when_position_instances_are_equal() {
        final Position position1 = new Position("1", "2");
        final Position position2 = new Position("1", "2");

        assertEquals(position1.hashCode(), position2.hashCode());
    }

    @Test
    public void test_should_return_string_with_position_fields() {
        final Position position = new Position("1", "2");

        assertEquals("Position(designationId=1, departmentId=2)", position.toString());
    }

    @Test
    public void test_should_return_false_when_both_position_instances_are_different() {
        final Position position1 = new Position("1", "3");
        final Position position2 = new Position("1", "2");

        assertFalse(position1.equals(position2));
    }

    @Test
    public void test_should_return_different_hash_code_when_position_instances_are_different() {
        final Position position1 = new Position("1", "2");
        final Position position2 = new Position("1", "3");

        assertNotEquals(position1.hashCode(), position2.hashCode());
    }

}