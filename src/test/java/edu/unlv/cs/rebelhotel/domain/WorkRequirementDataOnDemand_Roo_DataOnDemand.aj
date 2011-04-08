// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package edu.unlv.cs.rebelhotel.domain;

import edu.unlv.cs.rebelhotel.domain.WorkRequirement;
import java.util.List;
import java.util.Random;
import org.springframework.stereotype.Component;

privileged aspect WorkRequirementDataOnDemand_Roo_DataOnDemand {
    
    declare @type: WorkRequirementDataOnDemand: @Component;
    
    private Random WorkRequirementDataOnDemand.rnd = new java.security.SecureRandom();
    
    private List<WorkRequirement> WorkRequirementDataOnDemand.data;
    
    public WorkRequirement WorkRequirementDataOnDemand.getNewTransientWorkRequirement(int index) {
        edu.unlv.cs.rebelhotel.domain.WorkRequirement obj = new edu.unlv.cs.rebelhotel.domain.WorkRequirement();
        obj.setWorkTemplate(null);
        obj.setName("name_" + index);
        obj.setMilestone(true);
        return obj;
    }
    
    public WorkRequirement WorkRequirementDataOnDemand.getSpecificWorkRequirement(int index) {
        init();
        if (index < 0) index = 0;
        if (index > (data.size() - 1)) index = data.size() - 1;
        WorkRequirement obj = data.get(index);
        return WorkRequirement.findWorkRequirement(obj.getId());
    }
    
    public WorkRequirement WorkRequirementDataOnDemand.getRandomWorkRequirement() {
        init();
        WorkRequirement obj = data.get(rnd.nextInt(data.size()));
        return WorkRequirement.findWorkRequirement(obj.getId());
    }
    
    public boolean WorkRequirementDataOnDemand.modifyWorkRequirement(WorkRequirement obj) {
        return false;
    }
    
    public void WorkRequirementDataOnDemand.init() {
        data = edu.unlv.cs.rebelhotel.domain.WorkRequirement.findWorkRequirementEntries(0, 10);
        if (data == null) throw new IllegalStateException("Find entries implementation for 'WorkRequirement' illegally returned null");
        if (!data.isEmpty()) {
            return;
        }
        
        data = new java.util.ArrayList<edu.unlv.cs.rebelhotel.domain.WorkRequirement>();
        for (int i = 0; i < 10; i++) {
            edu.unlv.cs.rebelhotel.domain.WorkRequirement obj = getNewTransientWorkRequirement(i);
            obj.persist();
            obj.flush();
            data.add(obj);
        }
    }
    
}
