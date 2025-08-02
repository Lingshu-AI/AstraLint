const { createApp } = Vue;

createApp({
    data() {
        return {
            currentView: 'dashboard',
            pageTitle: '系统概览',
            loading: false,
            stats: {
                totalReviews: 0,
                totalIssues: 0,
                totalRepositories: 0,
                totalAiModels: 0
            },
            recentReviews: [],
            aiModels: [],
            repositories: [],
            settings: {
                aiServiceUrl: '',
                apiKey: '',
                maxFileSize: 10000,
                timeout: 30000
            },
            aiModelForm: {
                modelName: '',
                provider: 'ALIBABA',
                apiKey: '',
                temperature: 0.7,
                maxTokens: 4000,
                isActive: true,
                isDefault: false
            },
            repositoryForm: {
                repositoryName: '',
                repositoryType: 'GITLAB',
                repositoryUrl: '',
                accessToken: '',
                webhookSecret: '',
                isActive: true,
                autoReviewEnabled: true
            },
            editingModel: null,
            editingRepository: null
        }
    },
    mounted() {
        this.loadDashboard();
        this.loadAiModels();
        this.loadRepositories();
    },
    methods: {
        // 导航方法
        showDashboard() {
            this.currentView = 'dashboard';
            this.pageTitle = '系统概览';
            this.updateNavigation();
            this.loadDashboard();
        },
        
        showAiModels() {
            this.currentView = 'aiModels';
            this.pageTitle = 'AI模型管理';
            this.updateNavigation();
            this.loadAiModels();
        },
        
        showRepositories() {
            this.currentView = 'repositories';
            this.pageTitle = '仓库管理';
            this.updateNavigation();
            this.loadRepositories();
        },
        
        showSettings() {
            this.currentView = 'settings';
            this.pageTitle = '系统设置';
            this.updateNavigation();
        },
        
        updateNavigation() {
            // 更新导航状态
            document.querySelectorAll('.nav-link').forEach(link => {
                link.classList.remove('active');
            });
            
            const activeLink = document.querySelector(`[onclick="app.show${this.currentView.charAt(0).toUpperCase() + this.currentView.slice(1)}()"]`);
            if (activeLink) {
                activeLink.classList.add('active');
            }
        },
        
        // 数据加载方法
        async loadDashboard() {
            try {
                this.loading = true;
                const response = await axios.get('/api/admin/dashboard');
                const data = response.data;
                
                this.stats = {
                    totalReviews: data.totalReviews || 0,
                    totalIssues: data.totalIssues || 0,
                    totalRepositories: data.totalRepositories || 0,
                    totalAiModels: data.totalAiModels || 0
                };
                
                this.recentReviews = data.recentReviews || [];
            } catch (error) {
                console.error('加载仪表板数据失败:', error);
                this.showMessage('加载仪表板数据失败', 'error');
            } finally {
                this.loading = false;
            }
        },
        
        async loadAiModels() {
            try {
                this.loading = true;
                const response = await axios.get('/api/admin/ai-models');
                this.aiModels = response.data;
            } catch (error) {
                console.error('加载AI模型失败:', error);
                this.showMessage('加载AI模型失败', 'error');
            } finally {
                this.loading = false;
            }
        },
        
        async loadRepositories() {
            try {
                this.loading = true;
                const response = await axios.get('/api/admin/repositories');
                this.repositories = response.data;
            } catch (error) {
                console.error('加载仓库失败:', error);
                this.showMessage('加载仓库失败', 'error');
            } finally {
                this.loading = false;
            }
        },
        
        // AI模型管理方法
        showAddAiModelModal() {
            this.editingModel = null;
            this.aiModelForm = {
                modelName: '',
                provider: 'ALIBABA',
                apiKey: '',
                temperature: 0.7,
                maxTokens: 4000,
                isActive: true,
                isDefault: false
            };
            new bootstrap.Modal(document.getElementById('aiModelModal')).show();
        },
        
        editAiModel(model) {
            this.editingModel = model;
            this.aiModelForm = { ...model };
            new bootstrap.Modal(document.getElementById('aiModelModal')).show();
        },
        
        async saveAiModel() {
            try {
                const url = this.editingModel 
                    ? `/api/admin/ai-models/${this.editingModel.id}`
                    : '/api/admin/ai-models';
                const method = this.editingModel ? 'PUT' : 'POST';
                
                const response = await axios({
                    method,
                    url,
                    data: this.aiModelForm
                });
                
                bootstrap.Modal.getInstance(document.getElementById('aiModelModal')).hide();
                this.loadAiModels();
                this.showMessage('AI模型保存成功', 'success');
            } catch (error) {
                console.error('保存AI模型失败:', error);
                this.showMessage('保存失败: ' + (error.response?.data?.message || error.message), 'error');
            }
        },
        
        async deleteAiModel(id) {
            if (!confirm('确定要删除这个AI模型吗？')) return;
            
            try {
                await axios.delete(`/api/admin/ai-models/${id}`);
                this.loadAiModels();
                this.showMessage('AI模型删除成功', 'success');
            } catch (error) {
                console.error('删除AI模型失败:', error);
                this.showMessage('删除失败: ' + (error.response?.data?.message || error.message), 'error');
            }
        },
        
        // 仓库管理方法
        showAddRepositoryModal() {
            this.editingRepository = null;
            this.repositoryForm = {
                repositoryName: '',
                repositoryType: 'GITLAB',
                repositoryUrl: '',
                accessToken: '',
                webhookSecret: '',
                isActive: true,
                autoReviewEnabled: true
            };
            new bootstrap.Modal(document.getElementById('repositoryModal')).show();
        },
        
        editRepository(repo) {
            this.editingRepository = repo;
            this.repositoryForm = { ...repo };
            new bootstrap.Modal(document.getElementById('repositoryModal')).show();
        },
        
        async saveRepository() {
            try {
                const url = this.editingRepository 
                    ? `/api/admin/repositories/${this.editingRepository.id}`
                    : '/api/admin/repositories';
                const method = this.editingRepository ? 'PUT' : 'POST';
                
                const response = await axios({
                    method,
                    url,
                    data: this.repositoryForm
                });
                
                bootstrap.Modal.getInstance(document.getElementById('repositoryModal')).hide();
                this.loadRepositories();
                this.showMessage('仓库保存成功', 'success');
            } catch (error) {
                console.error('保存仓库失败:', error);
                this.showMessage('保存失败: ' + (error.response?.data?.message || error.message), 'error');
            }
        },
        
        async deleteRepository(id) {
            if (!confirm('确定要删除这个仓库吗？')) return;
            
            try {
                await axios.delete(`/api/admin/repositories/${id}`);
                this.loadRepositories();
                this.showMessage('仓库删除成功', 'success');
            } catch (error) {
                console.error('删除仓库失败:', error);
                this.showMessage('删除失败: ' + (error.response?.data?.message || error.message), 'error');
            }
        },
        
        async testConnection(repo) {
            try {
                const response = await axios.post(`/api/admin/repositories/${repo.id}/test`);
                if (response.data.success) {
                    this.showMessage('连接测试成功', 'success');
                } else {
                    this.showMessage('连接测试失败: ' + response.data.message, 'error');
                }
            } catch (error) {
                console.error('测试连接失败:', error);
                this.showMessage('测试连接失败: ' + (error.response?.data?.message || error.message), 'error');
            }
        },
        
        // 系统设置方法
        async saveSettings() {
            try {
                await axios.post('/api/admin/settings', this.settings);
                this.showMessage('设置保存成功', 'success');
            } catch (error) {
                console.error('保存设置失败:', error);
                this.showMessage('保存失败: ' + (error.response?.data?.message || error.message), 'error');
            }
        },
        
        // 工具方法
        refreshData() {
            switch (this.currentView) {
                case 'dashboard':
                    this.loadDashboard();
                    break;
                case 'aiModels':
                    this.loadAiModels();
                    break;
                case 'repositories':
                    this.loadRepositories();
                    break;
            }
        },
        
        getStatusBadgeClass(status) {
            return {
                'badge-success': status === 'COMPLETED',
                'badge-warning': status === 'PROCESSING',
                'badge-danger': status === 'FAILED'
            };
        },
        
        showMessage(message, type) {
            const alertDiv = document.createElement('div');
            alertDiv.className = `alert alert-${type === 'error' ? 'danger' : type} alert-dismissible fade show position-fixed`;
            alertDiv.style.cssText = 'top: 20px; right: 20px; z-index: 9999; min-width: 300px;';
            alertDiv.innerHTML = `
                ${message}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            `;
            
            document.body.appendChild(alertDiv);
            
            setTimeout(() => {
                if (alertDiv.parentNode) {
                    alertDiv.parentNode.removeChild(alertDiv);
                }
            }, 5000);
        }
    }
}).use(ElementPlus).mount('#app');