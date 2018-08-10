package com.silita.biaodaa.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.silita.biaodaa.common.MyRedisTemplate;
import com.silita.biaodaa.controller.vo.Page;
import com.silita.biaodaa.dao.*;
import com.silita.biaodaa.model.*;
import com.silita.biaodaa.utils.ObjectUtils;
import com.silita.biaodaa.utils.ProjectAnalysisUtil;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.silita.biaodaa.common.RedisConstantInterface.COM_OVER_TIME;
import static com.silita.biaodaa.common.RedisConstantInterface.PROJECT_LIST;

@Service
public class ProjectService {

    @Autowired
    TbProjectMapper tbProjectMapper;
    @Autowired
    TbCompanyMapper tbCompanyMapper;
    @Autowired
    TbProjectDesignMapper tbProjectDesignMapper;
    @Autowired
    TbProjectContractMapper tbProjectContractMapper;
    @Autowired
    TbProjectBuildMapper tbProjectBuildMapper;
    @Autowired
    TbProjectZhaotoubiaoMapper tbProjectZhaotoubiaoMapper;
    @Autowired
    TbProjectSupervisionMapper tbProjectSupervisionMapper;
    @Autowired
    TbProjectCompanyMapper tbProjectCompanyMapper;
    @Autowired
    MyRedisTemplate myRedisTemplate;
    @Autowired
    ProjectCompletionService projectCompletionService;

    private static ProjectAnalysisUtil projectAnalysisUtil = new ProjectAnalysisUtil();

    /**
     * 按工程查询
     *
     * @param param
     * @param result
     * @return
     */
    public Map<String, Object> getProjectList(Map<String, Object> param, Map<String, Object> result) {
        Integer pageNum = MapUtils.getInteger(param, "pageNo");
        Integer pageSize = MapUtils.getInteger(param, "pageSize");
        if (null != param.get("proType") && "房屋建筑".equals(param.get("proType"))) {
            param.put("proType", "房屋建筑工程");
        }
        String cacheKey = ObjectUtils.buildCacheKey(PROJECT_LIST, param);
        PageInfo pageInfo = (PageInfo) myRedisTemplate.getObject(cacheKey);
        if (null == pageInfo) {
            Page page = new Page();
            page.setPageSize(pageSize);
            page.setCurrentPage(pageNum);

            PageHelper.startPage(page.getCurrentPage(), page.getPageSize());
            List<Map<String, Object>> projectList = tbProjectMapper.queryObject(param);
            if (null != projectList && projectList.size() > 0) {
                for (Map<String, Object> map : projectList) {
                    this.putProvince(map);
                }
                pageInfo = new PageInfo(projectList);
                myRedisTemplate.setObject(cacheKey, pageInfo, COM_OVER_TIME);
            }
        }
        if (null != pageInfo) {
            result.put("data", pageInfo.getList());
            result.put("pageNum", pageInfo.getPageNum());
            result.put("pageSize", pageInfo.getPageSize());
            result.put("total", pageInfo.getTotal());
            result.put("pages", pageInfo.getPages());
        }
        return result;
    }

    /**
     * 按工程查看业绩列表
     * created by zhushuai
     *
     * @param params
     * @param result
     * @return
     */
    public Map<String, Object> getObjectListByUnit(Map<String, Object> params, Map<String, Object> result) {
        Integer pageNum = MapUtils.getInteger(params, "pageNo");
        Integer pageSize = MapUtils.getInteger(params, "pageSize");

        if (null == params.get("pactType")) {
            params.put("pactType", "施工");
        }

        Page page = new Page();
        page.setCurrentPage(pageNum);
        page.setPageSize(pageSize);

        PageHelper.startPage(page.getCurrentPage(), page.getPageSize());
        List<Map<String, Object>> projectList = tbProjectMapper.queryObjectByUnit(params);
        if (null != projectList && projectList.size() > 0) {
            for (Map<String, Object> map : projectList) {
                this.putProvince(map);
            }
        }
        PageInfo pageInfo = new PageInfo(projectList);
        result.put("data", pageInfo.getList());
        result.put("pageNum", pageInfo.getPageNum());
        result.put("pageSize", pageInfo.getPageSize());
        result.put("total", pageInfo.getTotal());
        result.put("pages", pageInfo.getPages());
        return result;
    }

    /**
     * 获取项目详情
     * created by zhushuai
     *
     * @param proId  项目id
     * @param result 返回result
     * @return
     */
    public Map<String, Object> getProjectDetail(String proId, Map<String, Object> result) {
        TbProject project = tbProjectMapper.queryProjectDetail(proId);
        if (null == project.getAcreage()) {
            if (null != project.getProScope()) {
                project.setAcreage(projectAnalysisUtil.analysisData(projectAnalysisUtil.regx, project.getProScope()));
            }
        }
        if (null != project.getInvestAmount() && project.getInvestAmount().contains("（万元）")) {
            project.setInvestAmount(project.getInvestAmount().substring(0, projectAnalysisUtil.getIndex(project.getInvestAmount(), "（")));
        }
        project.setAcreage(projectAnalysisUtil.getNumber(project.getAcreage()));
        result.put("data", project);
        return result;
    }

    /**
     * tab详情
     * created by zhushuai
     *
     * @param params
     * @param result
     * @return
     */
    public Map<String, Object> getTabProjectDetail(Map<String, Object> params, Map<String, Object> result) {
        String proId = MapUtils.getString(params, "proId");
        String tabType = MapUtils.getString(params, "tabType");
        List resultList = new ArrayList();
        PageInfo pageInfo = null;
        //施工图审查
        if ("design".equals(tabType)) {
            resultList = tbProjectDesignMapper.queryProjectDesignByProId(proId);
        } else if ("contract".equals(tabType)) {
            resultList = this.getContractList(params);
        } else if ("zhaotoubiao".equals(tabType)) {
            resultList = this.getZhaotoubiaoList(proId);
        } else if ("build".equals(tabType)) {
            resultList = this.getProjectBuildDetail(params);
        } else if ("completion".equals(tabType)) {
            resultList = projectCompletionService.getProjectCompletList(params);
        }
        result.put("data", resultList);
        if(null != resultList && resultList.size() > 0){
            result.put("total",resultList.size());
        }
        return result;
    }


    /**
     * 获取项目下的合同备案列表
     * created by zhushuai
     *
     * @param params
     * @return
     */
    private List<TbProjectContract> getContractList(Map<String, Object> params) {
        String proId = MapUtils.getString(params, "proId");
        List<TbProjectContract> list = tbProjectContractMapper.queryProjectContractListByProId(proId);
        if (null != list && list.size() > 0) {
            Map<String, Object> param = new HashMap<>();
            param.put("proId", proId);
            List<String> roleList = new ArrayList<>();
            roleList.add("发包单位");
            roleList.add("承包单位");
            param.put("roleList", roleList);
            param.put("type", "contract");
            List<TbProjectCompany> projectCompanyList = null;
            for (TbProjectContract projectContract : list) {
                param.put("pid", projectContract.getPkid());
                projectCompanyList = tbProjectCompanyMapper.queryProComList(param);
                if (null != projectCompanyList && projectCompanyList.size() > 0) {
                    for (TbProjectCompany company : projectCompanyList) {
                        if ("发包单位".equals(company.getRole())) {
                            projectContract.setLetContractComName(company.getComName());
                        } else if ("承包单位".equals(company.getRole())) {
                            projectContract.setContractComName(company.getComName());
                        }
                    }
                }
            }
        }
        return list;
    }

    private List<TbProjectZhaotoubiao> getZhaotoubiaoList(String proId) {
        List<TbProjectZhaotoubiao> list = tbProjectZhaotoubiaoMapper.queryZhaotoubiaoListByProId(proId);
//        if (null != list && list.size() > 0 && null != list.get(0)) {
//            return list;
//        }
        //施工表
//        List<TbProjectBuild> buildList = tbProjectBuildMapper.queryZhaobiaoProByProId(proId);
//        if (null != buildList && buildList.size() > 0 && null != buildList.get(0)) {
//            String remark = "";
//            String zhongbiaoDate = "";
//            TbProjectZhaotoubiao zhaotoubiao = null;
//            for (TbProjectBuild buid : buildList) {
//                remark = buid.getBidRemark();
//                zhaotoubiao = new TbProjectZhaotoubiao();
//                if (null != buid.getBidRemark() && !"未办理中标备案".equals(buid.getBidRemark())) {
//                    zhongbiaoDate = remark.substring(projectAnalysisUtil.getIndex(remark, "中标日期："), projectAnalysisUtil.getIndex(remark, ","));
//                    zhaotoubiao.setZhongbiaoDate(zhongbiaoDate == null ? null : zhongbiaoDate.substring(projectAnalysisUtil.getIndex(zhongbiaoDate, "：") + 1, zhongbiaoDate.length()));
//                }
//                zhaotoubiao.setProId(buid.getProId().toString());
//                zhaotoubiao.setZhongbiaoAmount(buid.getBidPrice());
//                zhaotoubiao.setZhaobiaoType(buid.getBidType());
//                zhaotoubiao.setZhongbiaoCompany(buid.getBOrg());
//                zhaotoubiao.setPkid(buid.getPkid());
//                list.add(zhaotoubiao);
//            }
//        }
        //监理表
//        List<TbProjectSupervision> projectSupervisionList = tbProjectSupervisionMapper.queryProjectSupervisionListByProId(proId);
//        if (null != projectSupervisionList && projectSupervisionList.size() > 0) {
//            String remark = "";
//            String zhongbiaoDate = "";
//            TbProjectZhaotoubiao zhaotoubiao = null;
//            String amount = null;
//            for (TbProjectSupervision proSup : projectSupervisionList) {
//                remark = proSup.getBidRemark();
//                zhaotoubiao = new TbProjectZhaotoubiao();
//                zhaotoubiao.setProId(proId.toString());
//                if (null != proSup.getBidRemark() && !"未办理中标备案".equals(proSup.getBidRemark())) {
//                    zhongbiaoDate = remark.substring(projectAnalysisUtil.getIndex(remark, "中标日期："), projectAnalysisUtil.getIndex(remark, ","));
//                    zhaotoubiao.setZhongbiaoDate(zhongbiaoDate == null ? null : zhongbiaoDate.substring(projectAnalysisUtil.getIndex(zhongbiaoDate, "：") + 1, zhongbiaoDate.length()));
//                    amount = remark.substring(projectAnalysisUtil.getIndex(remark, "中标价格："), projectAnalysisUtil.getIndex(remark, "万元"));
//                    zhaotoubiao.setZhongbiaoAmount(projectAnalysisUtil.isNumeric(amount) == true ? amount.substring(amount.lastIndexOf("中标价格：") + "中标价格：".length(), amount.length()) : null);
//                }
//                zhaotoubiao.setZhaobiaoType("监理");
//                zhaotoubiao.setZhongbiaoCompany(proSup.getSuperOrg());
//                zhaotoubiao.setPkid(proSup.getPkid());
//                list.add(zhaotoubiao);
//            }
//        }
        return list;
    }

    private List<TbProjectBuild> getProjectBuildDetail(Map params) {
        String proId = MapUtils.getString(params, "proId");
        List<TbProjectBuild> proBuildList = tbProjectBuildMapper.queryProjectBuildByProId(proId);
        return proBuildList;
    }

    /**
     * 将省信息放入集合中
     * created by zhushuai
     *
     * @param param
     */
    private void putProvince(Map<String, Object> param) {
        if (null != param.get("proWhere")) {
            String proWhere = param.get("proWhere").toString();
            param.put("province", projectAnalysisUtil.getProvince(proWhere));
        }
    }

    public List<Map<String, Object>> getProjectCompanyList(String companyId) {
        List<Map<String, Object>> resultMap = new ArrayList<>();

        // TODO:获取公司信息
        TbCompany company = tbCompanyMapper.getCompany(companyId);

        Map<String, Object> param = new HashMap<>();
        param.put("comName", company.getComName());
        param.put("orgCode", company.getOrgCode());

        //获取施工下的主项目
//        List<String> list = tbProjectBuildMapper.queryProIdByComId(companyId);
        //获取监理下的主项目
//        list.addAll(tbProjectSupervisionMapper.queryProIdByComId(companyId));
        //获取勘察设计的主项目
//        list.addAll(tbProjectDesignMapper.queryProIdByComId(companyId));

        //TODO: 获取主项目id
        List<String> list = tbProjectCompanyMapper.queryProIdForCom(param);

        //去掉重复的proId
        if (null != list && list.size() > 0) {
            Set<String> set = new HashSet(list);
            List<String> proList = new ArrayList<>(set);
            resultMap = tbProjectMapper.queryProjectListByIds(list);
        }
        if (null != resultMap && resultMap.size() > 0) {
            for (Map<String, Object> map : resultMap) {
                this.putProvince(map);
            }
        }
        return resultMap;
    }

    public void analysisData() {
        List<Map<String, Object>> paramList = new ArrayList<>();
        List<Map<String, Object>> proList = tbProjectMapper.queryObject(null);
        for (Map<String, Object> map : proList) {
            if (null != map.get("scope")) {
                String scope = map.get("scope").toString();
                map.put("acreage", projectAnalysisUtil.analysisData(projectAnalysisUtil.regx, scope));
                paramList.add(map);
            }
        }
        //修改
        System.out.println("size=================" + paramList.size());
        for (Map<String, Object> resMap : paramList) {
            System.out.println(resMap.get("scope") + "----------------------" + resMap.get("acreage"));
            if (null != resMap.get("acreage")) {
                if (projectAnalysisUtil.isNumeric(resMap.get("acreage").toString())) {
                    tbProjectMapper.update(resMap);
                }
            }
        }
    }
}
