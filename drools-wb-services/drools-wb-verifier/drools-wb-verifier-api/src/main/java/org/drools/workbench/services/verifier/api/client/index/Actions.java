/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.drools.workbench.services.verifier.api.client.index;

import java.util.Collection;

import org.drools.workbench.services.verifier.api.client.index.matchers.ExactMatcher;
import org.drools.workbench.services.verifier.api.client.index.matchers.KeyMatcher;
import org.drools.workbench.services.verifier.api.client.index.matchers.Matcher;
import org.drools.workbench.services.verifier.api.client.index.select.Listen;
import org.drools.workbench.services.verifier.api.client.index.select.Select;
import org.drools.workbench.services.verifier.api.client.maps.KeyTreeMap;

public class Actions {

    private final KeyTreeMap<Action> map = new KeyTreeMap<>( Action.keyDefinitions());

    public Actions() {

    }

    public Actions( final Collection<Action> actions ) {
        for ( final Action action : actions ) {
            add( action );
        }
    }

    public void add( final Action action ) {

        map.put( action );
    }

    public void merge( final Actions actions ) {
        map.merge( actions.map );
    }

    public Where<ActionSelect, ActionListen> where( final Matcher matcher ) {
        return new Where<ActionSelect, ActionListen>() {
            @Override
            public ActionSelect select() {
                return new ActionSelect( matcher );
            }

            @Override
            public ActionListen listen() {
                return new ActionListen( matcher );
            }
        };
    }

    public <KeyType> MapBy<KeyType, Action> mapBy( final KeyMatcher matcher ) {
        return new MapBy<>( map.get( matcher.getKeyDefinition() ) );
    }

    public void remove( final Column column ) {
        final ExactMatcher matcher = Action.columnUUID().is( column.getUuidKey() );
        for ( final Action action : where( matcher ).select().all() ) {
            action.getUuidKey().retract();
        }
    }

    public class ActionSelect
            extends Select<Action> {

        public ActionSelect( final Matcher matcher ) {
            super( map.get( matcher.getKeyDefinition() ),
                   matcher );
        }
    }

    public class ActionListen
            extends Listen<Action> {

        public ActionListen( final Matcher matcher ) {
            super( map.get( matcher.getKeyDefinition() ),
                   matcher );
        }
    }
}
