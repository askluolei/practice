<template>
  <div class="entity-audit-wrapper">
    <h2 v-text="$t('entityAudit.home.title')">Entity Audit</h2>

    <h4 v-text="$t('entityAudit.home.filter')">Filter</h4>
    <el-row>
      <span v-text="$t('entityAudit.home.entityOrTable')">Entity/Table</span>
      <el-select v-model="selectedEntity">
        <el-option v-for="entity of entities" :key="entity" :value="entity"></el-option>
      </el-select>
      <span v-text="$t('entityAudit.home.limitTo')">Limit to</span>
      <el-select v-model="limit">
        <el-option v-for="lim of limits" :key="lim" :value="lim"></el-option>
      </el-select>
      <el-button :disabled="!selectedEntity" @click="getEntityChanges" type="primary" v-text="$t('entityAudit.home.load')">Load</el-button>
    </el-row>
    <el-row v-if="loaded">
      <p>Last <strong>{{limit}}</strong> Changes for <strong>{{selectedEntity}}</strong></p>
    </el-row>
    <el-row>
      <span v-text="$t('entityAudit.result.searchFieldLabel')">Filter:</span>
      <el-input id="filter" v-model="filter" placeholder="entity id filter"></el-input>
    </el-row>
    <el-table :data="entitiesView" stripe>
      <el-table-column prop="entityId" :label="$t('entityAudit.result.tableHeader.entityId')"></el-table-column>
      <el-table-column prop="action" :label="$t('entityAudit.result.tableHeader.action')"></el-table-column>
      <el-table-column prop="commitVersion" :label="$t('entityAudit.result.tableHeader.version')"></el-table-column>
      <el-table-column prop="entityValue" :label="$t('entityAudit.result.tableHeader.value')">
        <template slot-scope="prop">
          <pre>{{ prop.row.entityValue }}</pre>
        </template>
      </el-table-column>
      <el-table-column prop="modifiedDate" :label="$t('entityAudit.result.tableHeader.modifiedDate')"></el-table-column>
      <el-table-column prop="modifiedBy" :label="$t('entityAudit.result.tableHeader.modifiedBy')"></el-table-column>
      <el-table-column :label="$t('entityAudit.result.tableHeader.viewDetail')">
        <template slot-scope="prop">
          <el-button @click="entityPrevious(prop.row)" icon="el-icon-view"></el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog :visible.sync="dialog" :title="$t('entityAudit.detail.title')">
      <el-row>
        <strong v-text="currentEntity.action + ' ' + $t('entityAudit.detail.action')"></strong>
      </el-row>
      <el-row>
        <span @click="versionChange('old')"><el-tag :type="selectVersion === 'old' ? 'success': 'info'" v-text="$t('entityAudit.detail.old')">Old/Removed value</el-tag></span>
        <span @click="versionChange('new')"><el-tag :type="selectVersion === 'new' ? 'success': 'info'" v-text="$t('entityAudit.detail.new')">New/Added value</el-tag></span>
      </el-row>
      <el-row>
        <pre>{{selectVersion === 'old' ? previousEntity.entityValue : currentEntity.entityValue}}</pre>
      </el-row>
    </el-dialog>
  </div>
</template>

<script>
  import { entities, entityChanges, entityPrevious } from '@/api/administrator'
  export default {
    data() {
      return {
        entities: [],
        selectedEntity: '',
        filter: '',
        limits: [25, 50, 100, 200],
        limit: 25,
        loaded: false,
        data: [],
        previousEntity: {},
        currentEntity: {},
        selectVersion: 'old',
        dialog: false
      }
    },
    computed: {
      entitiesView() {
        if (this.filter) {
          const result = []
          this.data.forEach(entity => {
            if (String(entity.entityId) === this.filter) {
              result.push(entity)
            }
          })
          return result
        } else {
          return this.data
        }
      }
    },
    methods: {
      versionChange(v) {
        this.selectVersion = v
      },
      entityPrevious(entity) {
        if (entity.commitVersion < 2) {
          this.$message({
            message: '没有之前的数据版本，这是第一个版本',
            type: 'warning'
          })
          return
        }
        entityPrevious({
          qualifiedName: entity.entityType,
          entityId: entity.entityId,
          commitVersion: entity.commitVersion
        })
          .then(response => {
            this.currentEntity = entity
            const data = response.data
            this.previousEntity = data
            this.dialog = true
          })
      },
      getEntityChanges() {
        if (this.selectedEntity && this.limit) {
          entityChanges({
            entityType: this.selectedEntity,
            limit: this.limit
          })
            .then(response => {
              const data = response.data
              this.data = data
            })
        }
      },
      getEntities() {
        entities()
          .then(response => {
            const data = response.data
            this.entities = data
          })
      },
      init() {
        this.getEntities()
      }
    },
    created() {
      this.init()
    }
  }
</script>

<style lang="scss">
  .entity-audit-wrapper {
    margin: 10px 20px;
  }
</style>