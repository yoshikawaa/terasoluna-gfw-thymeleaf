/**
 * Copyright (c) 2017 Atsushi Yoshikawa (https://yoshikawaa.github.io)
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
package io.github.yoshikawaa.gfw.web.thymeleaf.expression;

import org.terasoluna.gfw.web.el.Functions;

/**
 * Expression utility object for generating query parameters.
 * 
 * @author Atsushi Yoshikawa
 */
public class Query {

    /**
     * Generate query parameters using {@link Functions#query(Object)}.
     * 
     * @param params generating source
     * @return generated query parameters without {@code ?} separator.
     * @see Functions#query(Object)
     */
    public String params(Object params) {
        return Functions.query(params);
    }
}
