/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.ranger.tagsync.source.atlasrest;

import org.apache.atlas.type.AtlasClassificationType;
import org.apache.atlas.type.AtlasStructType;
import org.apache.atlas.type.AtlasTypeRegistry;
import org.apache.commons.lang.StringUtils;
import org.apache.ranger.tagsync.source.atlas.EntityNotificationWrapper;

import java.util.Map;

public class RangerAtlasEntityWithTags {
    private final RangerAtlasEntity entity;
    private final Map<String, Map<String, String>> tags;
    private final AtlasTypeRegistry typeRegistry;

    public RangerAtlasEntityWithTags(EntityNotificationWrapper notification ) {

        this.entity = notification.getRangerAtlasEntity();

        this.tags = notification.getAllClassifications();

        this.typeRegistry = null;
    }

    public RangerAtlasEntityWithTags(RangerAtlasEntity entity, Map<String, Map<String, String>> tags, AtlasTypeRegistry typeRegistry) {
        this.entity = entity;
        this.tags = tags;
        this.typeRegistry = typeRegistry;
    }

    public RangerAtlasEntity getEntity() {
        return entity;
    }

    public Map<String, Map<String, String>> getTags() {
        return tags;
    }

	public String getTagAttributeType(String tagTypeName, String tagAttributeName) {
		String ret = StringUtils.EMPTY;

		if (typeRegistry != null) {
			AtlasClassificationType classificationType = typeRegistry.getClassificationTypeByName(tagTypeName);
			if (classificationType != null) {
				AtlasStructType.AtlasAttribute atlasAttribute = classificationType.getAttribute(tagAttributeName);

				if (atlasAttribute != null) {
					ret = atlasAttribute.getTypeName();
				}
			}
		}

		return ret;
	}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (entity != null) {
            sb.append("{entity=").append(entity).append(", ");
        }
        sb.append("tags={");
        for (Map.Entry<String, Map<String, String>> tag : tags.entrySet()) {
            sb.append("{tagName=").append(tag.getKey());
            sb.append(", attributes={");
            for (Map.Entry<String, String> attribute : tag.getValue().entrySet()) {
                sb.append("{attributeName=").append(attribute.getKey());
                sb.append(",attributeValue=").append(attribute.getValue());
                sb.append("}");
            }
            sb.append("}");
            sb.append("}");
        }
        return sb.toString();
    }
}