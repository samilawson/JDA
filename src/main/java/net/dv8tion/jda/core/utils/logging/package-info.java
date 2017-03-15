/*
 *     Copyright 2015-2017 Austin Keener & Michael Ritter
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

/**
 * SLF4J Logger implementation
 * that can optionally be used in JDA.
 *
 * <p>To use this implementation you have to provide
 * a <a href="https://www.slf4j.org/api/org/slf4j/impl/StaticLoggerBinder.html" target="_blank">org.slf4j.impl.StaticLoggerBinder</a>
 * and return the {@link net.dv8tion.jda.core.utils.logging.JDALoggerFactory JDALoggerFactory}
 * as the {@link org.slf4j.ILoggerFactory ILoggerFactory}!
 */
package net.dv8tion.jda.core.utils.logging;
