<template>
	<div>
		<el-input placeholder="输入关键字进行过滤" v-model="filterText" style="width: 300px;">
		</el-input>
		<div v-if="currentNodeName!==''">
			<label style="size: letter">当前选择项目：</label>
      <el-tag size="small" style="color: black; background-color: #eeeeee; border-style: solid; border-width: 1px; margin-right: 10px">
        {{currentNodeName}}（{{currentNodeId}}）
      </el-tag>
      <br>
      <label style="color: red; font-size: 12px">单选优先级高于多选</label>
		</div>
		<br />
		<!--default-expand-all-->
		<el-tree class="filter-tree"
				 show-checkbox
				 node-key="id"
				 default-expand-all
				 @node-click="handleNodeClick"
				 @check-change="handleCheckChange"
				 @check="handleCheck"
				 highlight-current
				 :data="data" :props="defaultProps" :filter-node-method="filterNode" ref="tree" :check-on-click-node="false">
		</el-tree>
		<br />
		<br />
		<el-button @click="addNode" size="mini">添加节点</el-button>
		<el-checkbox v-model="checked">根节点</el-checkbox>
		<br />
		<el-button @click="getNode" size="mini" v-if="false">获取节点</el-button>
		<el-button @click="getId" size="mini" v-if="false">获取id</el-button>
    <br />
		<el-button @click="clear" size="mini">清空(多选)</el-button>
		<el-button @click="reset" size="mini">重置(单选)</el-button>
		<el-button @click="deletes" size="mini">删除</el-button>

		<!--新增节点-->
		<el-dialog
				title="新增节点"
				:visible.sync="showAddNode"
				width="50%"
				:before-close="handleCloseAddItem">
			  <span slot="footer" class="dialog-footer">
				  <el-form :model="ruleFormItem" :rules="rulesItem" ref="ruleFormItem" label-width="100px" class="demo-ruleForm">
					  <el-form-item label="父节点id" prop="pId" v-if='!checked'>
						<el-input v-model="ruleFormItem.pId" :disabled="true"></el-input>
					  </el-form-item>
					  <el-form-item label="父节点名称" prop="pName" v-if='!checked'>
						<el-input v-model="ruleFormItem.pName" :disabled="true"></el-input>
					  </el-form-item>
					  <el-form-item label="名称" prop="name">
						<el-input v-model="ruleFormItem.name"></el-input>
					  </el-form-item>
					  <el-form-item label="备注" prop="remark">
						<el-input type="textarea" v-model="ruleFormItem.remark"></el-input>
					  </el-form-item>

					  <el-form-item>
						  	<el-button @click="quxiao">取 消</el-button>
							<el-button type="primary" @click="submitFormItem('ruleFormItem')">保存</el-button>
							<el-button @click="resetFormItem('ruleFormItem')">重置</el-button>
					  </el-form-item>
					</el-form>
			  </span>
		</el-dialog>
	</div>

</template>
<script>
	module.exports = {
		data: function() {
			return {
        props: {
          currentDate: '',
        },
				// url: 'http://127.0.0.1:8082',
				url: '',
				checked: true,
				login: {
					userId: ''
				},
				defaultProps: {
					children: 'child',
					label: 'name'
				},
				filterText: '',
				data: [],

				currentNodeName: '',
				currentNodeId: '',
        currentCheckedNodeIds: [],

				// 添加节点
				showAddNode: false,
				ruleFormItem: {
					name: '',
					pId: '',
					pName: '',
					remark: ''
				},
				rulesItem: {
					name: [
						{ required: true, message: '请输入节点名称', trigger: 'change' }
					],
					remark: [
						{ required: true, message: '请输入备注', trigger: 'change' }
					],
					pId: [
						{ required: true, message: '父id不能为空', trigger: 'change' }
					]
				},
			}
		},
		created() {
			// this.login.userId = localStorage.getItem("login-userName");
      this.custIps();
		},
		watch: {
			filterText(val) {
				this.$refs.tree.filter(val);
			}
		},
		mounted() {

    },
		methods: {

      // 获取客户ip
      custIps() {
        axios.post(this.url + '/login/custIp', {}).then(response => {
          if (response.data.success) {
            // this.login.userId = response.data.data;
            console.log("获取客户IP",response.data.data)
            this.login.userId = localStorage.getItem("login-userName");
            this.treeData();
          } else {
            alert(response.data.msg);
          }
        }).catch(error => {
          console.error(error);
          alert('获取客户ipmmm');
        });
      },

			addNode(){
				// 非根节点
				if(!this.checked){
					if(this.currentNodeId===''){
						alert('添加非根节点，请用单选功能选择父节点');
						return;
					}
					this.ruleFormItem.pId=this.currentNodeId;
					this.ruleFormItem.pName=this.currentNodeName;
				}else{
					this.ruleFormItem.pId='-1';
					this.ruleFormItem.pName='';
				}
				console.log("111222",this.ruleFormItem.pId);
				console.log(this.ruleFormItem.pName);

				this.showAddNode=true;
			},
			// 新增字典项提交表单
			submitFormItem(formName) {
				this.$refs[formName].validate((valid) => {
					if (valid) {
						// 校验
						this.add().then((res)=>{
								console.log("data..", res);
								console.log("data....", res.data.data);
								if(res.data.code === 200) {
									alert("菜单新增成功");
									this.resetFormItem();
								}
							this.treeData();
						});
					} else {
						console.log('error submit!!');
						return false;
					}
				});
			},
			add(){
				return axios.post(
						this.url + '/shop/add', {
							pId: this.ruleFormItem.pId,
							pName: this.ruleFormItem.pName,
							name: this.ruleFormItem.name,
							remark: this.ruleFormItem.remark,
							tentId: this.login.userId
						})
				// 		.then(res => {
				// 	console.log("data..", res);
				// 	console.log("data....", res.data.data);
				// 	if(res.data.code == 200) {
				// 		alert("菜单新增成功");
				// 		this.resetFormItem();
				// 	}
				// 	// alert(JSON.stringify(this.res));
				// })
			},
			// 重置新增节点窗口
			resetFormItem() {
				this.ruleFormItem.name = '';
				this.ruleFormItem.remark = '';
			},
			// 关闭新增窗口
			handleCloseAddItem(done) {
				this.$confirm('确认关闭？')
						.then(_ => {
							this.showAddNode=false;
							this.currentNodeId='';
							this.currentNodeName='';
							console.log("关闭新增-function");
							console.log(this.currentNodeName);
							this.treeData();
						})
						.catch(_ => {});
			},
			quxiao(){
				this.showAddNode = false;
				this.currentNodeId='';
				this.currentNodeName='';
				console.log("quxiao-function");
				console.log(this.currentNodeName);
				this.treeData();
			},
			getId() {
				console.log(this.$refs.tree.getCheckedKeys());
			},
			getNode() {
				console.log(this.$refs.tree.getCheckedNodes());
			},
			clear() {
				this.$refs.tree.setCheckedKeys([]);
        var clearCheckedKeys = this.$refs.tree.getCheckedKeys();
        console.log("清空后获取多选节点数据", clearCheckedKeys)
        this.currentCheckedNodeIds = this.$refs.tree.getCheckedKeys();
        this.$emit("clear-click", clearCheckedKeys);
      },
      reset() {
        this.$refs.tree.setCurrentKey(null);
        this.currentNodeId = "";
        this.currentNodeName = "";
        this.$emit("reset-click", this.currentNodeId, this.currentCheckedNodeIds);
      },
			handleNodeClick(data, a, s) {
				console.log("单击选中树的一项");
				this.currentNodeName = data.name;
				this.currentNodeId = data.id;
        this.$emit("node-click", data.id, this.currentCheckedNodeIds);
				console.log(data, a, s);
				console.log(a);
				console.log(s);
			},
      handleCheckChange(data, checked, indeterminate) {
        console.log("子组件多选，展示数据");
        console.log(data, checked, indeterminate);
      },
      handleCheck(checkedNodes, checkedNodesKeys) {
        console.log('复选框被点击', checkedNodes, checkedNodesKeys);
        this.currentCheckedNodeIds = this.$refs.tree.getCheckedKeys();
        console.log(this.currentCheckedNodeIds);
        // 这里可以执行你需要的操作
        // alert("复选框被单击");
        this.$emit("checkbox-click", this.currentNodeId, this.currentCheckedNodeIds);
      },
			filterNode(value, data) {
				if(!value) return true;
				return data.name.indexOf(value) !== -1;
			},

			treeData() {
				axios.post(
						this.url + '/shop/menu?tentId=' + this.login.userId, {
							tentId: "admin"
						}).then(res => {
					console.log("data..", res);
					console.log("data....", res.data.data);
					if(res.data.code === 200) {
						this.data = res.data.data;
					}
					// alert(JSON.stringify(this.res));
				})
			},

			deletes() {
				var list0 = "";
				this.$refs.tree.getCheckedKeys().forEach(item => list0 = list0 + item.toString() + ",");
				return axios.post(
						this.url + '/shop/delete?list='+list0, {
							list: this.$refs.tree.getCheckedKeys()
						}).then(res => {
					console.log("delete.res=", res);
					if(res.data.code === 200) {
						alert("删除成功");
						this.currentNodeId='';
						this.currentNodeName='';
						console.log("deletes-function");
						console.log(this.currentNodeName);

						this.treeData();
            // 删除节点后，传给父组件
            this.currentCheckedNodeIds = this.$refs.tree.getCheckedKeys();
            console.log(">>?/",this.currentNodeId,this.currentCheckedNodeIds);
            console.log(this.currentCheckedNodeIds);
            this.$emit("deletes-click", this.currentNodeId, this.currentCheckedNodeIds);

					}
					if(res.data.code === 400) {
						alert(res.data.msg+",请用多选框进行选择");
					}
				})
			},

		}
	}
</script>