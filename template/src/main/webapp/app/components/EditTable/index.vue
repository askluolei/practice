<template>
    <!-- 这个组件是一个基础组件 -->
    <!-- 有个问题，貌似子组件的事件 只有这里能够收到，如果这里不处理，外层组件就收不到事件 -->
    <div class="editor-table-wrapper">

        <h2 class="title-wrapper" v-if="title">{{title}}</h2>
        <!-- 搜索区 -->
        <el-col :span="24" v-if="activeConfig.search.show && queryFormProperties.length !== 0" class="search-wrapper">
            <slot name="queryForm">
                <template>
                    <el-form :model="editRecord.queryForm" ref="queryForm" :inline="true" class="seachForm">
                        <el-form-item v-for="item of queryFormProperties" :key="item" :label="tableDataConfig[item].label" class="searchItem">
                            <slot :name="'queryForm_' + item" :form="editRecord.queryForm" :item="item">
                                <!-- 这里加个插槽 可以修改默认的样式  命名插槽 以 queryForm_ 开头，后面是 字段名 -->
                                <template v-if="tableDataConfig[item].type === 'text'">
                                    <el-input v-model="editRecord.queryForm[item]"></el-input>
                                </template>
                                <template v-if="tableDataConfig[item].type === 'select'">
                                    <el-select v-model="editRecord.queryForm[item]">
                                        <el-option :key="'empty'" :label="'--请选择--'" :value="''"></el-option>
                                        <el-option v-for="op of optionsValues(tableDataConfig[item], editRecord.queryForm)" :key="op.value" :label="op.label" :value="op.value"></el-option>
                                    </el-select>
                                </template>
                                <template v-if="tableDataConfig[item].type === 'date'">
                                    <el-date-picker v-model="editRecord.queryForm[item]" align="right" type="date" placeholder="选择日期" :picker-options="commons.pickerOptions"></el-date-picker>
                                </template>
                            </slot>
                        </el-form-item>
                        <el-form-item>
                            <el-button @click="handleOnQuerySubmit">查询</el-button>
                            <el-button @click="resetForm(editRecord.queryForm)">重置</el-button>
                        </el-form-item>
                    </el-form>
                </template>
            </slot>
        </el-col>

        <!-- 功能按钮区 -->
        <el-col :span="24" v-if="activeConfig.tools.show" class="edit-wrapper">
            <el-button-group>
                <el-button type="primary" icon="plus" v-if="activeConfig.tools.plus" @click="handleAdd"></el-button>
                <el-button type="primary" icon="delete" v-if="activeConfig.tools.delete" @click="handleDeleteSeleted"></el-button>
                <el-button type="primary" icon="edit" v-if="activeConfig.tools.edit" @click="handleEditSelected"></el-button>
                <el-button type="primary" icon="search" v-if="activeConfig.tools.view" @click="handleView"></el-button>
                <!-- 这里留一个扩展点，添加扩展按钮组 -->
                <slot name="buttonGroup"></slot>
            </el-button-group>
        </el-col>

        <!-- 数据展示区 -->
        <el-col :span="24" v-if="activeConfig.dataView.show" class="table-wrapper">
            <el-table :data="tableData" highlight-current-row :border="activeConfig.elConfig.border" :default-sort="activeConfig.elConfig.defaultSort" :show-summary="activeConfig.elConfig.showSummary" :sum-text="activeConfig.elConfig.sumText" :summary-method="activeConfig.elConfig.summaryMethod" @selection-change="handleSelectionChange" @current-change="handleCurrentRowChange">
                <!-- 全选列 -->
                <el-table-column type="selection" v-if="activeConfig.selection"></el-table-column>

                <!-- 数据列 -->
                <el-table-column v-for="item of tableHeader" :width="tableDataConfig[item].width" :min-width="tableDataConfig[item].minWidth" v-if="tableDataConfig[item].tableShow" :key="item" :prop="item" :label="tableDataConfig[item].label">
                    <template scope="scope">
                        <!-- 具名插槽 -->
                        <slot v-if="tableDataConfig[item].type !== 'date'" :name="item" :itemKey="item" :row="tableData[scope.$index]">
                            {{(scope.row[item] && tableDataConfig[item].cascadeShow)? scope.row[item][tableDataConfig[item].cascadeShow] : scope.row[item]}}
                        </slot>
                        <slot v-if="tableDataConfig[item].type === 'date'" :name="item" :itemKey="item" :row="tableData[scope.$index]">
                            <el-icon name="time"></el-icon>
                            <span style="margin-left: 10px">{{scope.row[item]}}</span>
                        </slot>
                    </template>
                </el-table-column>

                <!-- 尾部扩展列 -->
                <template v-if="activeConfig.append.show">
                    <el-table-column v-for="column of activeConfig.append.columns" :width="activeConfig.append.columnConfig[column].width" :min-width="activeConfig.append.columnConfig[column].minWidth" :key="column" :label="activeConfig.append.columnConfig[column].label">
                        <template scope="scope">
                            <!-- 具名插槽 -->
                            <slot :name="column" :itemKey="column" :index="scope.$index" :row="tableData[scope.$index]" :config="activeConfig.append.columnConfig[column]">
                                {{scope.$index}}
                            </slot>
                        </template>
                    </el-table-column>
                </template>

                <!-- 操作列 -->
                <el-table-column v-if="activeConfig.operation.show" label="操作" :width="activeConfig.operation.width" :min-width="activeConfig.operation.minWidth">
                    <template scope="scope">
                        <el-button v-if="activeConfig.operation.edit" size="small" icon="edit" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
                        <el-button v-if="activeConfig.operation.delete" size="small" icon="delete" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
                    </template>
                </el-table-column>

                
            </el-table>
        </el-col>

        <!-- 分页区 -->
        <el-col :span="24" v-if="activeConfig.page.show" class="page-wrapper">
            <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page.sync="editRecord.page.currentPage" :page-sizes="editRecord.page.pageSizes" :page-size="editRecord.page.pageSize" layout="sizes, prev, pager, next" :total="tablePage.total"></el-pagination>
        </el-col>

        <!-- dialog 区 -->
        <el-dialog :title="dialog[dialog.type].title" :visible.sync="dialog.show" class="dialog-wrapper">
            <el-row>
                <el-form :model="editRecord.form" ref="form" :rules="editRecord.formRule" :label-position="activeConfig.dataView.labelPosition">
                    <el-col :span="12" v-for="item of formProperties" :key="item">
                        <el-form-item :prop="item" :label="tableDataConfig[item].label" label-width="30%" class="dialogItem">
                            <slot :name="'form_' + item" :item="item" :form="editRecord.form" :type="dialog.type">
                                <!-- 这里加个具名插槽 以 form_开头 允许修改form表单样式 -->
                                <template>
                                    <el-input v-if="tableDataConfig[item].type === 'text'" :disabled="dialog.type === 'view' || (dataConfig[item].readonly && dialog.type === 'edit')" v-model="editRecord.form[item]" :readonly="dataConfig[item].readonly && dialog.type === 'edit'"></el-input>
                                </template>
                                <template>
                                    <el-input type="textarea" v-if="tableDataConfig[item].type === 'textarea'" :disabled="dialog.type === 'view' || (dataConfig[item].readonly && dialog.type === 'edit')" v-model="editRecord.form[item]" :readonly="dataConfig[item].readonly && dialog.type === 'edit'"></el-input>
                                </template>
                                <template>
                                    <el-input type="password" v-if="tableDataConfig[item].type === 'password'" :disabled="dialog.type === 'view' || (dataConfig[item].readonly && dialog.type === 'edit')" v-model="editRecord.form[item]" :readonly="dataConfig[item].readonly && dialog.type === 'edit'"></el-input>
                                </template>
                                <template v-if="tableDataConfig[item].type === 'select'">
                                    <el-select v-model="editRecord.form[item]" :disabled="dialog.type === 'view' || (dataConfig[item].readonly && dialog.type === 'edit')">
                                        <el-option :key="'empty'" :label="'--请选择--'" :value="''"></el-option>
                                        <el-option v-for="op of optionsValues(tableDataConfig[item], editRecord.form)" :key="op.value" :label="op.label" :value="op.value"></el-option>
                                    </el-select>
                                </template>
                                <template v-if="tableDataConfig[item].type === 'date'">
                                    <el-date-picker :disabled="dialog.type === 'view' || (dataConfig[item].readonly && dialog.type === 'edit')" v-model="editRecord.form[item]" align="right" type="date" placeholder="选择日期" :picker-options="commons.pickerOptions"></el-date-picker>
                                </template>
                                <template v-if="tableDataConfig[item].type === 'checkbox'">
                                    <el-checkbox-group v-model="editRecord.form[item]" :min="tableDataConfig[item].boxMin" :max="tableDataConfig[item].boxMax" :disabled="dialog.type === 'view' || (dataConfig[item].readonly && dialog.type === 'edit')">
                                        <el-checkbox v-for="box of tableDataConfig[item].boxs" :key="box" :label="box">{{box}}</el-checkbox>
                                    </el-checkbox-group>
                                </template>
                            </slot>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6" :offset="16" v-if="dialog.type !== 'view'">
                        <el-form-item class="save-button">
                            <el-button type="primary" @click="handleOnSubmit">{{dialog[dialog.type].submitText}}</el-button>
                            <el-button @click="resetForm(editRecord.form)">重置</el-button>
                            <el-button @click="handleOnCancel">取消</el-button>
                        </el-form-item>
                    </el-col>
                </el-form>
            </el-row>
        </el-dialog>
    </div>
</template>

<script>

//验证
function isPhone(str) {
    if(!str) return;
    var reg = /^(0|86|17951)?(13[0-9]|15[012356789]|18[0-9]|14[57]|17[678])[0-9]{8}$/;
    if(reg.test(str.trim())) {
        return;
    } else {
        return '不合法的手机号';
    }
}

function isQQ(str) {
    if(!str) return;
    // 1 首位不能是0  ^[1-9]
    // 2 必须是 [5, 11] 位的数字  \d{4, 9}
    var reg = /^[1-9][0-9]{4,9}$/gim;
    if (reg.test(str.trim())) {
        return;
    } else {
        return '不合法的QQ号';
    }
}

function isEmail(str) {
    if(!str) return;
    var reg = /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/;
    if(reg.test(str.trim())) {
        return;
    } else {
        return '不合法的邮箱';
    }
}

function isNumber(str) {
    if(!str) return;
    var reg = /^\d+$/;
    if (reg.test(str.trim())) {
        return;
    } else {
        return '不合法的整数';
    }
}

function isIDcard(str) {
    if(!str) return;
    // 起始数字不能为0，然后是5个数字  [1-9]\d{5}
    var reg = /^(^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$)|(^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])((\d{4})|\d{3}[Xx])$)$/;
    if (reg.test(str.trim())) {
        return;
    } else {
        return '不合法的身份证号';
    }
}

function isIP(str) {
    if(!str) return;
    var reg = /^([1-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\.([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])){3}$/gi;
    if (reg.test(str.trim())) {
        return;
    } else {
        return '不合法的IP';
    }
}

//大小写字母
function isLetter(str) {
    if(!str) return;
    const reg = /^[A-Za-z]+$/;
    if (reg.test(str)) {
        return;
    } else {
        return '不合法的字母';
    }
}

// 4到16位 大小写字母数字下划线
function isUserName(str) {
    if(!str) return;
    const reg = /^[a-zA-Z0-9_-]{4,16}$/;
    if (reg.test(str.trim())) {
        return;
    } else {
        return '4-16位大小写字母数字下划线';
    }
}

function isPassword(str) {
    if(!str) return;
    const reg = /^[a-zA-Z\w]{6,18}$/;
    if (reg.test(str.trim())) {
        return;
    } else {
        return '6-18位大小写字母和数字';
    }
}

function isStrongPass(str) {
    if(!str) return;
    const reg = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,10}$/
    if (reg.test(str.trim())) {
        return;
    } else {
        return '8-10位大小写字母和数字';
    }
}

//中文、英文、数字包括下划线
function isCnEn(str) {
    if(!str) return;
    const reg = /^[\u4E00-\u9FA5A-Za-z0-9_]{3,20}$/;
    if (reg.test(str.trim())) {
        return;
    } else {
        return '3-20位中文、英文、数字包括下划线组合';
    }
}

//汉字
function isCn(str) {
    if(!str) return;
    const reg = /^[\u4e00-\u9fa5]{0,}$/
    if (reg.test(str.trim())) {
        return;
    } else {
        return '只能有汉字';
    }
}

function isCnName(str) {
    if(!str) return;
    const reg = /^[\u4e00-\u9fa5]{0,10}$/
    if (reg.test(str.trim())) {
        return;
    } else {
        return '0-10位汉字';
    }
}

const defaultDataConfig = {
    readonly: false,//只读
    nullable: true,//可空
    editShow: true,//编辑的时候是否显示
    viewShow: true,//查看时候是否显示
    editable: true,//是否可编辑
    createShow: true,//新建记录的时候是否显示
    defaultVal: undefined,//新增的时候默认值
    type: 'text',//操作数据
    dependOn: [],//当type 为 select 的时候，如果有级联时候的依赖关系
    //当type 为 select 的时候展示的选项 可以为数组（里面是对象，必须要有 value 和 label） 对象 （key 为值，value 为要展示的列表）, 函数 （动态的或者级联）
    options: [],//注意的一点是，如果依赖一个字段，当初始的时候，这个字段可能没有值，注意判断这个条件，返回要给空数组就行了
    tableShow: true,//再table中是否展示 在列很多的时候，只展示关键信息，其他信息可以点击查看按钮来获取详细信息,
    order: 100,//排序 值小的排在前面,
    formType: 'String',//用来支持 多选扩展 可选 Array
    fillForm: undefined,//同名字段填充form的时候，自定义转换 这里应该是方法 有返回值
    width: undefined,//宽度  数值
    minWidth: undefined,//最小宽度 字符串 百分比 这两个操作也允许在操作列上和扩展列上配置
    cascadeShow: undefined,//如果有级联关系那么对象里面就套对象了，这里表示的是展示内部对象的列 注意，暂时不支持嵌套
    cascadeValue: undefined,//如果有嵌套关系，那么表单中的取值，取对象中的哪个字段
    rules: {},//字段校验规则 对象中添加一个 normal:[] 数组里面可以填通用校验，或者继续添加key：function(val) 自己校验 返回值为空的时候校验通过，否则，认为返回的信息就是要展示的信息 
}

export default {
    //使用的局部组件
    components: {

    },
    //组件内部数据
    data() {
        return {
            //表头字段 就是 key 数组 认为这里面对应的key才显示，其他的都不展示
            tableHeader: [],
            //如果是内部实现增删改查功能，那么数据将有内部管理
            innerTableData: [],
            //激活的 全局配置 用户配置 默认配置的merge
            activeConfig: {
                //el-table 的配置属性
                elConfig: {
                    //是否为斑马纹 table
                    stripe: false,
                    //是否带有纵向边框
                    border: false,
                    //列的宽度是否自撑开
                    fit: false,
                    //默认的排序列的prop和顺序。它的prop属性指定默认的排序的列，order指定默认排序的顺序 order: ascending, descending
                    //例子
                    defaultSort: {},
                    //是否在表尾显示合计行 默认情况下，对于合计行，第一列不进行数据求合操作，而是显示「合计」二字（可通过sum-text配置）
                    showSummary: false,
                    //看上一行配置
                    sumText: '合计',
                    //统计方法 接受 对象参数 里面有 columns 和 data 字段 返回一个数组 对应每列显示啥
                    summaryMethod: undefined,
                    //行的 className 的回调方法，Function(row, index)/String 也可以使用字符串为所有行设置一个固定的 className。
                    rowClassName: undefined,
                    //行的 style 的回调方法，Function(row, index)/Object 也可以使用一个固定的 Object 为所有行设置一样的 Style。
                    rowStyle: undefined,
                    //空数据时显示的文本内容
                    emptyText: ''
                },
                //全选
                selection: true,
                //搜索栏
                search: {
                    show: true,
                    prepend: {

                    }
                },
                //功能栏
                tools: {
                    show: true,
                    plus: true,
                    delete: true,
                    edit: true,
                    view: true
                },
                //数据展示区
                dataView: {
                    show: true,
                    labelPosition: 'right'
                },
                //操作列
                operation: {
                    show: true,
                    edit: true,
                    delete: true
                },
                //尾部扩展
                append: {
                    show: false,
                    columns: [],
                    columnConfig: {}
                },
                //分页数据
                page: {
                    show: true
                },
                //可以将功能方法传进来 
                funcs: {
                    //是否启用，如果启用了，那么就不需要绑定data了，数据将由这个组件来管理
                    on: false,

                    //对应增删改查 由于是与后台交互，这里方法的返回要是 Promise
                    add: undefined, //预期 返回  {}
                    delete: undefined,//
                    edit: undefined,
                    query: undefined,

                    preQuery: undefined,
                    preEdit: undefined,//修改数据前
                    preAdd: undefined,//新增数据前
                    preDelete: undefined,//删除数据方法前
                    
                    //预处理返回结果 正常情况下，上面的方法返回结果认为就是数据，没有任何包装, add 返回添加记录 delete 返回删除的记录 edit 返回修改的记录 query 返回page
                    //如果后台统一保证了一层，例如统一有 errorCode errorMsg data ，其中data里面是上面说的数据，那么可以给一个预处理方法 返回预期的data内容
                    pre: undefined,
                    //映射 作用是处理前端和后端字段名不一致的情况， key是 前端字段名，value是后端字段名，譬如
                    mapping: {
                        currentPage: 'currentPage',//当前页
                        pageSize: 'pageSize',//页大小
                        content: 'content',//page 中获取查询到的数据
                    }
                },
                //这里加一些通用的前端验证
                validate: {
                    require: {required: true, message: '不能为空', trigger: 'blur'},
                    number: isNumber,
                    phone: isPhone,
                    username: isUserName,
                    qq: isQQ,
                    email: isEmail,
                    idCard: isIDcard,
                    ip: isIP,
                    letter: isLetter,
                    password: isPassword,
                    enCn: isCnEn,
                    cn: isCn,
                    cnName: isCnName
                }
            },
            //当前数据记录
            editRecord: {
                //分页记录
                page: {
                    currentPage: 1,
                    pageSizes: [10, 20, 50],
                    pageSize: 10,
                    total: undefined
                },
                //当前选择行 这个是多选栏的当前选择框架
                multipleSelection: [],
                //这个就是光标选择的行
                currentRow: undefined,
                //内部修改字段的时候不触发watch
                innerEditFlag: false,
                //编辑表单model 增，改的时候用
                form: {

                },
                formRule: {

                },
                //查询表单
                queryForm: {

                },
                //select的依赖关系
                depends: {

                },
                watch: {},
                unwatch: []
            },
            //弹出框相关  增，改， 查看也可以弹出，但是不允许修改
            dialog: {
                show: false,
                //当前表单类型
                type: 'create',
                //dialog 关闭延时时间
                closeDelay: 1000,
                create: {
                    title: '新增',
                    submitText: '创建'
                },
                edit: {
                    title: '编辑',
                    submitText: '保存'
                },
                view: {
                    title: '查看',
                    submitText: '默认'
                }
            },
            commons: {
                pickerOptions: {
                    shortcuts: [{
                        text: '今天',
                        onClick(picker) {
                            picker.$emit('pick', new Date());
                        }
                    }, {
                        text: '昨天',
                        onClick(picker) {
                            const date = new Date();
                            date.setTime(date.getTime() - 3600 * 1000 * 24);
                            picker.$emit('pick', date);
                        }
                    }, {
                        text: '一周前',
                        onClick(picker) {
                            const date = new Date();
                            date.setTime(date.getTime() - 3600 * 1000 * 24 * 7);
                            picker.$emit('pick', date);
                        }
                    }]
                }
            }
        }
    },
    //接受外部传入数据
    props: {
        //标题 : ''
        title: {
            type: String,
            require: false
        },
        //展示的数据: [{key: '' || key: {value:''}}]
        //数据列表，通常是后端返回的数据
        data: {
            type: Array,
            require: false
        },
        //数据相关的配置 就是针对每列数据的
        dataConfig: {
            type: Object,
            require: true
        },
        //分页相关
        //里面需要有个 total
        page: {
            type: Object,
            require: false
        },
        //可以看做全局配置
        config: {
            type: Object,
            require: false
        },
        //通知，如果增删改查放到内部了，外部通知内部组件用的，目前只支持 query 
        //使用方法是 绑定 notify 里面包含 {query:1} ，改变query的值，将触发内部的查询（第一页，带上当前查询条件）
        notify: {
            type: Object,
            require: false
        }
    },
    //所有功能方法都将触发事件
    methods: {
        //初始化dataConfig
        initDataConfig() {
            //初始化dataConfig
            //这里读取依赖关系
            for (let key of Object.keys(this.$props.dataConfig)) {
                let config = this.$props.dataConfig[key];
                if (config.type === 'select') {
                    if (config.dependOn) {
                        for (let de of config.dependOn) {
                            if (this.editRecord.depends[de] && this.editRecord.depends[de].indexOf(key) === -1) {
                                this.editRecord.depends[de].push(key);
                            } else {
                                this.editRecord.depends[de] = [];
                                this.editRecord.depends[de].push(key);
                            }
                        }
                    }
                }
            }
            let dependKeys = Object.keys(this.editRecord.depends);
            if (dependKeys.length > 0) {
                for (let key of dependKeys) {
                    function callbackFactory(pre, formModel, depends) {
                        if (formModel && formModel.indexOf('.') !== -1) {
                            let index = formModel.indexOf('.');
                            let start = formModel.substring(0, index);
                            let end = formModel.substring(index + 1);
                            return callbackFactory(pre[start], end, depends);
                        } else {
                            return function(val, oldVal) {
                                if (val !== oldVal) {
                                    for (let deKey of depends) {
                                        if (pre[formModel][deKey]) {
                                            pre[formModel][deKey] = '';
                                        }
                                    }
                                }
                            }
                        }
                    }

                    //设置监视
                    const formModels = ['editRecord.form', 'editRecord.queryForm'];
                    for (let formModel of formModels) {
                        let _key = formModel + '.' + key;
                        let _callback = callbackFactory(this.$data, formModel, this.editRecord.depends[key]);
                        this.editRecord.watch[_key] = _callback;
                        this.editRecord.unwatch.push(this.$watch(formModel + '.' + key, _callback));
                    }
                    
                }
            }
        },

        //初始化数据
        initAll() {
            this.tableHeader = Object.keys(this.$props.dataConfig);
            let _this = this;
            this.tableHeader = this.tableHeader.sort((a, b) => {
                let aOrder = _this.tableDataConfig[a].order;
                let bOrder = _this.tableDataConfig[b].order;
                return aOrder - bOrder;
            });

            //初始化 activeConfig 注意 只支持两层嵌套,两层以后直接就引用
            if (this.config) {
                for (let key of Object.keys(this.config)) {
                    let config1 = this.config[key];
                    let v1 = this.activeConfig[key];
                    if (v1 instanceof Object && config1 instanceof Object) {
                        for (let key2 of Object.keys(config1)) {
                            this.activeConfig[key][key2] = config1[key2];
                        }
                    } else {
                        this.activeConfig[key] = config1;
                    }
                }
            }

            this.initDataConfig();

            //分页相关 TODO
            if (this.page && this.page.total) {
                this.editRecord.page.total = this.page.total;
            }

            //内部管理增删改查 的时候初始化数据
            if (this.activeConfig.funcs.on) {
                this.innerQuery(1);//内部管理数据初始化
            }

            //初始化表单验证规则
            this.initRule();
            
        },
        //
        initRule() {
            for (let key of this.tableHeader) {
                if (!this.tableDataConfig[key].nullable) {
                    this.addRule(this.editRecord.formRule, key, this.activeConfig.validate.require);
                }

                //如果还存在其他验证
                if(this.tableDataConfig[key].rules) {
                    for (let ruleKey of Object.keys(this.tableDataConfig[key].rules)) {
                        if (ruleKey === 'normal') {
                            for (let n of this.tableDataConfig[key].rules.normal) {
                                this.addRule(this.editRecord.formRule, key, this.activeConfig.validate[n]);
                            }
                        } else {
                            let customValidate = this.tableDataConfig[key].rules[ruleKey];
                            if (customValidate instanceof Function) {
                                this.addRule(this.editRecord.formRule, key, customValidate);
                            }
                        }
                    }
                }
            }
        },

        addRule(rule, key, ruleValid) {
            if (ruleValid instanceof Function) {
                this.addValidate(rule, key, {validator: (rule, value, callback) => {
                    let errorMsg = ruleValid(value);
                    if (errorMsg) {
                        callback(new Error(errorMsg));
                    } else {
                        callback();
                    }
                }, trigger: 'blur'})
            } else {
                this.addValidate(rule, key, ruleValid);
            }
        },

        addValidate(rule, key, ruleValid) {
            if (rule[key]) {
                rule[key].push(ruleValid);
            } else {
                this.$set(rule, key, []);
                rule[key].push(ruleValid);
            }
        },

        optionsValues({ options, dependOn }, form) {
            let _result = [];
            if (options instanceof Array) {
                for (let op of options) {
                    if (op instanceof Object) {
                        //如果是对象，则必须要有 value label
                        _result.push(op);
                    } else {
                        //否则就当做 字符串或者数字了，不检查
                        _result.push({
                            label: op,
                            value: op
                        })
                    }
                }
            } else if (options instanceof Function) {// Function 也是Object 因此，先判断 Function  Array 也是一样
                let params = {}
                if (dependOn && dependOn.length > 0) {
                    for (let dep of dependOn) {
                        params[dep] = form[dep];
                    }
                }
                let ret = options(params);//调用，然后
                _result = this.optionsValues({
                    options: ret,
                    dependOn
                }, form);//递归调用
            } else if (options instanceof Object) {
                //如果是对象
                for (let key of Object.keys(options)) {
                    _result.push({
                        value: key,
                        label: options[key]
                    })
                }
            }
            return _result;
        },

        unwatchForm() {
            for (let f of this.editRecord.unwatch) {
                f();
            }
        },
        addWatch() {
            this.editRecord.unwatch = [];
            for (let key of Object.keys(this.editRecord.watch)) {
                this.editRecord.unwatch.push(this.$watch(key, this.editRecord.watch[key]));
            }
        },
        //填充dialog表单
        fillForm(row) {
            this.unwatchForm();
            if (row) {
                for (let key of this.formProperties) {
                    let value;
                    if (this.tableDataConfig[key].cascadeValue && row[key]) {
                        value = row[key][this.tableDataConfig[key].cascadeValue];
                    } else {
                        value = row[key];
                    }
                    let fillForm = this.tableDataConfig[key].fillForm;
                    if ('Array' === this.tableDataConfig[key].formType) {
                        let valIsArray = value instanceof Array;
                    }
                    if (fillForm) {
                        this.$set(this.editRecord.form, key, fillForm(value, row));
                    } else {
                        this.$set(this.editRecord.form, key, value);
                    }
                }
            } else {
                for (let key of this.formProperties) {
                    let defaultVal = this.tableDataConfig[key].defaultVal;
                    if ('Array' === this.tableDataConfig[key].formType) {
                        if (defaultVal) {
                             this.$set(this.editRecord.form, key, defaultVal);
                        } else {
                            this.$set(this.editRecord.form, key, []);
                        }
                       
                    } else {
                        if (defaultVal) {
                            this.$set(this.editRecord.form, key, defaultVal);
                        } else {
                            this.$set(this.editRecord.form, key, '');
                        }
                    }
                }
            }
            this.addWatch();
            // this.editRecord.form = form;
        },

        //重置表单
        resetForm(form) {
            for (let key of Object.keys(form)) {
                if (this.dialog.type === 'edit' && this.tableDataConfig[key].readonly) {
                    continue;
                }
                if ('Array' === this.tableDataConfig[key].formType) {
                    form[key] = [];
                } else {
                    form[key] = '';
                }
            }
        },
        //关闭dialog 默认1s后关闭
        closeDialog() {
            let _that = this;
            setTimeout(function() {
                _that.dialog.show = false;
            }, _that.dialog.closeDelay);
        },
        //处理映射，获得对应参数
        handleMapping(pageNum, pageSize, form) {
            let params = {};
            let currentPage = pageNum;
            let mapping = this.activeConfig.funcs.mapping;
            if (pageNum) {
                if (mapping.currentPage) {
                    params[mapping.currentPage] = currentPage;
                } else {
                    params.currentPage = currentPage
                }
            }

            if (pageSize) {
                if (mapping.pageSize) {
                    params[mapping.pageSize] = pageSize;
                } else {
                    params.pageSize = pageSize;
                }
            }

            if (form) {
                for (let key of Object.keys(form)) {
                    if (mapping[key]) {
                        params[mapping[key]] = form[key];
                    } else {
                        params[key] = form[key];
                    }
                }
            }
            return params;
        },
        //处理查询返回的data
        handleBack(data) {
            let mapping = this.activeConfig.funcs.mapping;
            if (mapping.content) {
                this.innerTableData = data[mapping.content];
            } else {
                this.innerTableData = data.content;
            }
            if (mapping.total) {
                this.editRecord.page.total = data[mapping.total];
            } else {
                this.editRecord.page.total = data.total;
            }
            this.$emit('data', this.innerTableData);
        },
        //使用内部查询
        innerQuery(pageNum) {
            let currentPage = pageNum || this.editRecord.page.currentPage;
            let pageSize = this.editRecord.page.pageSize;
            let params = this.handleMapping(currentPage, pageSize, this.editRecord.queryForm);
            if (this.activeConfig.funcs.preQuery) {
                params = this.activeConfig.funcs.preQuery(params);
            }
            let _this = this;
            this.activeConfig.funcs.query(params)
                .then(response => {
                    let data = undefined;
                    if (_this.activeConfig.funcs.pre) {
                        data = _this.activeConfig.funcs.pre(response);
                    } else {
                        data = response;
                    }
                    this.editRecord.page.currentPage = currentPage;
                    _this.handleBack(data);
                }).catch(error => {
                    let message = '操作异常' + (error || '');
                    _this.$message.error(message);
                })
        },
        handleCascadeMapping() {
            let params = Object.assign({}, this.editRecord.currentRow, this.editRecord.form);
            for (let key of Object.keys(params)) {
                if (!key) continue;
                let mappingKey = this.activeConfig.funcs.mapping[key];
                let cascadeValue = this.tableDataConfig[key] ? this.tableDataConfig[key].cascadeValue : undefined;
                if (this.formProperties.indexOf(key) != -1) {
                    if(mappingKey) {
                        params[mappingKey] = params[key];
                    }
                } else {
                    if (cascadeValue) {
                        if (mappingKey) {
                            params[mappingKey] = params[key][cascadeValue];
                        } else {
                            params[key] = params[key][cascadeValue];
                        }
                    } else {
                        if (mappingKey) {
                            params[mappingKey] = params[key];
                        }
                    }
                }
            }
            return params;
        },
        //新增
        innerAdd() {
            let params = this.handleCascadeMapping();
            if (this.activeConfig.funcs.preAdd) {
                params = this.activeConfig.funcs.preAdd(params);
            }
            let _this = this;
            this.activeConfig.funcs.add(params)
                .then(response => {
                    let data = undefined;
                    if (_this.activeConfig.funcs.pre) {
                        data = _this.activeConfig.funcs.pre(response);
                    } else {
                        data = response;
                    }
                    _this.$message({
                        showClose: true,
                        message: '新增成功',
                        type: 'success'
                    });
                    _this.closeDialog();
                    _this.innerQuery(1);//重新查询一次  TODO 暂时先这样
                }).catch(error => {
                    let message = '操作异常' + (error || '');
                    _this.$message({
                        showClose: true,
                        message: message,
                        type: 'error'
                    });
                });
        },
        //删除
        innerDelete(rows) {
            console.log('row', rows);
            if (!rows) return;
            let params;
            if (this.activeConfig.funcs.preDelete) {
                console.log('preDelete')
                params = this.activeConfig.funcs.preDelete(rows);
            } else {
                params = rows;
            }
            let _this = this;
            this.activeConfig.funcs.delete(params)
                .then(response => {
                    let data = undefined;
                    if (_this.activeConfig.funcs.pre) {
                        data = _this.activeConfig.funcs.pre(response);
                    } else {
                        data = response;
                    }
                    _this.innerQuery(1);//重新查询一次  TODO 暂时先这样

                    _this.$message('删除成功');
                }).catch(error => {
                    let message = '操作异常' + (error || '');
                    _this.$message({
                        showClose: true,
                        message: message,
                        type: 'error'
                    });
                });
        },
        //修改
        innerEdit() {
            let params = this.handleCascadeMapping();
            if (this.activeConfig.funcs.preEdit) {
                params = this.activeConfig.funcs.preEdit(params);
            }
            let _this = this;
            this.activeConfig.funcs.edit(params)
                .then(response => {
                    let data = undefined;
                    if (_this.activeConfig.funcs.pre) {
                        data = _this.activeConfig.funcs.pre(response);
                    } else {
                        data = response;
                    }
                    _this.$message({
                        showClose: true,
                        message: '修改成功',
                        type: 'success'
                    });
                    _this.closeDialog();
                    _this.innerQuery(1);//重新查询一次  TODO 暂时先这样
                }).catch(error => {
                    let message = '操作异常' + (error || '');
                    _this.$message({
                        showClose: true,
                        message: message,
                        type: 'error'
                    });
                });
        },
        shouldInnerQuery() {
            return this.activeConfig.funcs.on && this.activeConfig.funcs.query;
        },
        shouldInnerAdd() {
            return this.activeConfig.funcs.on && this.activeConfig.funcs.add;
        },
        shouldInnerDelete() {
            return this.activeConfig.funcs.on && this.activeConfig.funcs.delete;
        },
        shouldInnerEdit() {
            return this.activeConfig.funcs.on && this.activeConfig.funcs.edit;
        },
        //查询表单提交
        handleOnQuerySubmit() {
            if (this.shouldInnerQuery()) {
                this.innerQuery();
            }
            this.$emit('search', this.editRecord.queryForm);
            this.$message({
                showClose: true,
                message: '查询成功',
                type: 'success'
            });
        },

        //表单提交
        handleOnSubmit() {
            this.$refs.form.validate(valid => {
                if (valid) {
                    this.$emit('submit', this.dialog.type, this.editRecord.form);//触发提交事件
                    if (this.dialog.type === 'create') {
                        if (this.shouldInnerAdd()) {
                            this.innerAdd();
                        }
                    } else if (this.dialog.type = 'edit') {
                        if (this.shouldInnerEdit()) {
                            this.innerEdit();
                        }
                    }
                } else {
                    return false;
                }
            })
        },

        //表单取消提交
        handleOnCancel() {
            this.dialog.show = false;
        },

        //功能栏 新增按钮  弹出 Dialog
        handleAdd() {
            this.dialog.type = 'create';
            this.dialog.show = true;
            this.fillForm();
        },

        //功能栏 查看按钮  弹出 Dialog
        handleView() {
            this.dialog.type = 'view';
            if (this.editRecord.currentRow) {
                this.fillForm(this.editRecord.currentRow);
                this.dialog.type = 'view';
                this.dialog.show = true;
            } else if (this.editRecord.multipleSelection && this.editRecord.multipleSelection.length === 1) {
                let row = this.editRecord.multipleSelection[0];
                this.fillForm(row);
                this.dialog.type = 'view';
                this.dialog.show = true;
            } else {
                return;
            }
        },

        // 分页相关
        // pageSize 改变
        handleSizeChange(val) {
            this.editRecord.page.pageSize = val;
            this.editRecord.page.currentPage = 1;//切换size的时候，每次都回到第一页
            this.$emit('page', this.editRecord.page, this.editRecord.queryForm);//触发分页事件，外部应该监听这个事件后，查询数据
            if (this.shouldInnerQuery()) {
                this.innerQuery();
            }
        },

        // currentPage 改变
        handleCurrentChange(val) {
            this.$emit('page', this.editRecord.page, this.editRecord.queryForm);
            if (this.shouldInnerQuery()) {
                this.innerQuery();
            }
        },

        //编辑 对应每行后的编辑按钮
        handleEdit(index, row) {
            // 触发监听
            this.fillForm(row);
            this.dialog.type = 'edit';
            this.dialog.show = true;
            this.editRecord.currentRow = row;
        },

        //编辑选中行 只有选且选中1行 才触发
        handleEditSelected() {
            if (!this.editRecord.multipleSelection || this.editRecord.multipleSelection.length !== 1) {
                this.$message('请点击行最后一列的删除或者勾选第一列');
                return;
            }
            let row = this.editRecord.multipleSelection[0];
            this.fillForm(row);
            this.dialog.type = 'edit';
            this.dialog.show = true;
        },

        //删除 对应每行后的删除按钮
        handleDelete(index, row) {
            this.$confirm('此操作将删除本行数据，操作不可回滚，是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.$emit('delete', [row]);
                if (this.shouldInnerDelete()) {
                    this.innerDelete([row]);
                }
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消删除'
                });
            });
        },

        //删除选中行
        handleDeleteSeleted() {
            if (!this.editRecord.multipleSelection || this.editRecord.multipleSelection.length === 0) {
                this.$message('请点击行最后一列的删除或者勾选第一列');
                return;
            }
            this.$confirm('此操作将删除本行数据，操作不可回滚，是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.$emit('delete', this.editRecord.multipleSelection);
                if (this.shouldInnerDelete()) {
                    this.innerDelete(this.editRecord.multipleSelection);
                }
            }).catch((error) => {
                this.$message({
                    type: 'info',
                    message: '已取消删除'
                });
            });
        },

        //当前选择行改变
        handleSelectionChange(val) {
            this.editRecord.multipleSelection = val;
        },

        //当前行改变
        handleCurrentRowChange(currentRow, oldVal) {
            this.editRecord.currentRow = currentRow;
        }
    },
    //计算属性
    computed: {
        tablePage: function() {
            let result = {};
            if (this.activeConfig.funcs.on) {
                result.total = Number(this.editRecord.page.total) || this.tableData.length;
            } else {
                if (this.$props.page) {
                    result.total = Number(this.$props.page.total) || this.$props.data.length;
                } else {
                    result.total = this.$props.data.length;
                }
                
            }
            return result;
        },
        //数据配置
        tableDataConfig: function() {
            let config = {};
            //初始化dataConfig
            for (let key of Object.keys(this.$props.dataConfig)) {
                config[key] = Object.assign({}, defaultDataConfig, this.$props.dataConfig[key]);
            }
            return config;
        },
        //表格展示区
        tableData: function() {
            if (this.activeConfig.funcs.on) {
                return this.innerTableData || [];
            } else {
                if (this.$props.data) {
                    let pageSize = this.editRecord.page.pageSize;
                    let currentPage = this.editRecord.page.currentPage;
                    let startIndex = pageSize * (currentPage - 1);
                    let endIndex = pageSize * currentPage;
                    return this.$props.data.slice(startIndex, endIndex);
                } else {
                    return []
                }
            }
        },

        //表单需要显示的字段
        formProperties: function() {
            let _result = [];
            if (this.dialog.type === 'create') {
                for (let key of this.tableHeader) {
                    let config = this.$props.dataConfig[key];
                    if (config && config.hasOwnProperty('createShow') && !config.createShow) {
                    } else {
                        _result.push(key);
                    }
                }
            } else if (this.dialog.type === 'edit') {
                for (let key of this.tableHeader) {
                    let config = this.$props.dataConfig[key];
                    if (config && config.hasOwnProperty('editShow') && !config.editShow) {
                    } else {
                        _result.push(key);
                    }
                }
            } else {
               for (let key of this.tableHeader) {
                    let config = this.$props.dataConfig[key];
                    if (config && config.hasOwnProperty('viewShow') && !config.viewShow) {
                    } else {
                        _result.push(key);
                    }
                }
            }
            
            return _result;
        },
        //查询栏的表单显示的内容
        queryFormProperties: function() {
            let _result = [];
            for (let key of this.tableHeader) {
                let config = this.$props.dataConfig[key];
                if (config && config.search) {
                    //有配置是查询字段才显示
                    _result.push(key);
                }
            }
            return _result;
        }
    },
    //数据过滤器
    filters: {

    },
    //监视数据变化
    watch: {
        'page.total': function(val) {
            this.editRecord.page.total = val;
        },
        'notify.query': function(val) {
            if (this.shouldInnerQuery()) {
                this.innerQuery();
            }
        }
    },
    //组件创建的时候触发
    created() {
        this.initAll();
    },
    //组件挂载的时候触发
    mounted() {

    }
}
</script>

<style lang="scss" scoped>
    .editor-table-wrapper {
        margin: 10px 20px;

        .title-wrapper {

        }

        .search-wrapper {

            .seachForm {

                .searchItem {

                }
            }
        }

        .edit-wrapper {

        }       

        .table-wrapper {

        } 

        .page-wrapper {

        }

    }

    .dialog-wrapper {
        .dialogItem {
            .el-input {
                width: 80%;
            }
            .el-select {
                width: 80%;
            }
        }

        .save-button {
            margin-top: 20px;
        }
    }

</style>
