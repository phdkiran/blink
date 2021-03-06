package com.nashlincoln.blink.model;

import java.util.List;
import com.nashlincoln.blink.model.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
import com.nashlincoln.blink.app.BlinkApp;
import com.nashlincoln.blink.network.BlinkApi;
import com.nashlincoln.blink.nfc.NfcCommand;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import com.nashlincoln.blink.event.Event;
// KEEP INCLUDES END
/**
 * Entity mapped to table BLINK_GROUP.
 */
public class Group {

    private String name;
    private Long id;
    private String attributableType;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient GroupDao myDao;

    private List<GroupDevice> groupDeviceList;
    private List<Attribute> attributes;

    // KEEP FIELDS - put your custom fields here
    public static final String KEY = "Group";
    public static final String ATTRIBUTABLE_TYPE = "Group";
    // KEEP FIELDS END

    public Group() {
    }

    public Group(Long id) {
        this.id = id;
    }

    public Group(String name, Long id, String attributableType) {
        this.name = name;
        this.id = id;
        this.attributableType = attributableType;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getGroupDao() : null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAttributableType() {
        return attributableType;
    }

    public void setAttributableType(String attributableType) {
        this.attributableType = attributableType;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<GroupDevice> getGroupDeviceList() {
        if (groupDeviceList == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            GroupDeviceDao targetDao = daoSession.getGroupDeviceDao();
            List<GroupDevice> groupDeviceListNew = targetDao._queryGroup_GroupDeviceList(id);
            synchronized (this) {
                if(groupDeviceList == null) {
                    groupDeviceList = groupDeviceListNew;
                }
            }
        }
        return groupDeviceList;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetGroupDeviceList() {
        groupDeviceList = null;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<Attribute> getAttributes() {
        if (attributes == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            AttributeDao targetDao = daoSession.getAttributeDao();
            List<Attribute> attributesNew = targetDao._queryGroup_Attributes(id, attributableType);
            synchronized (this) {
                if(attributes == null) {
                    attributes = attributesNew;
                }
            }
        }
        return attributes;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetAttributes() {
        attributes = null;
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

    // KEEP METHODS - put your custom methods here

    public void deleteWithReferences() {
        BlinkApp.getDaoSession().runInTx(new Runnable() {
            @Override
            public void run() {
                for (GroupDevice groupDevice : getGroupDeviceList()) {
                    groupDevice.delete();
                }

                for (Attribute attribute : getAttributes()) {
                    attribute.delete();
                }

                delete();
            }
        });

        Event.broadcast(Group.KEY);
    }

    public static Group newInstance() {
        Group group = new Group();
        group.setAttributableType(ATTRIBUTABLE_TYPE);
        return group;
    }

    public void copyAttributes(final List<Attribute> attributes) {
        final AttributeDao attributeDao = BlinkApp.getDaoSession().getAttributeDao();
//        BlinkApp.getDaoSession().runInTx(new Runnable() {
//            @Override
//            public void run() {
                for (Attribute attribute : attributes) {
                    Attribute attr = new Attribute();
                    attr.setAttributableType(ATTRIBUTABLE_TYPE);
                    attr.setAttributableId(getId());
                    attr.setAttributeType(attribute.getAttributeType());
                    attr.setValue(attribute.getValue());
                    attributeDao.insert(attr);
                    getAttributes().add(attr);
                }
//            }
//        });
        resetAttributes();
    }

    public List<Device> getDevices() {
        List<Device> devices = new ArrayList<>();
        for (GroupDevice groupDevice : getGroupDeviceList()) {
            devices.add(groupDevice.getDevice());
        }
        return devices;
    }

    public void setLevel(final int level) {
        BlinkApp.getDaoSession().runInTx(new Runnable() {
            @Override
            public void run() {
                Attribute attribute = getAttributes().get(1);
                attribute.setValue(String.valueOf(level));
                attribute.update();

                for (Device device : getDevices()) {
                    device.setLevel(level);
                }
            }
        });
    }

    public void setOn(final boolean on) {
        BlinkApp.getDaoSession().runInTx(new Runnable() {
            @Override
            public void run() {
                Attribute attribute = getAttributes().get(0);
                attribute.setValue(on ? Attribute.ON : Attribute.OFF);
                attribute.update();

                for (Device device : getDevices()) {
                    device.setOn(on);
                }
            }
        });
    }

    public boolean isOn() {
        return getAttributes().get(0).getBool();
    }

    public int getLevel() {
        return getAttributes().get(1).getInt();
    }

    public void updateDevices() {
        BlinkApp.getDaoSession().runInTx(new Runnable() {
            @Override
            public void run() {
                for (Device device : getDevices()) {
                    device.setOn(isOn());
                    device.setLevel(getLevel());
                }
            }
        });
    }

    public String toNfc() {
        List<NfcCommand> commands = new ArrayList<>();
        List<NfcCommand.Update> updates = new ArrayList<>();

        for (Attribute attribute : getAttributes()) {
            NfcCommand.Update update = new NfcCommand.Update();
            update.i = attribute.getAttributeTypeId();
            update.v = attribute.getValue();
            updates.add(update);
        }

        for (Device device : getDevices()) {
            NfcCommand command = new NfcCommand();
            command.i = device.getId();
            command.u = updates;
            commands.add(command);
        }

        return BlinkApi.getGson().toJson(commands);
    }

    public void addDevice(long deviceId) {
        DeviceDao deviceDao = daoSession.getDeviceDao();
        Device device = deviceDao.load(deviceId);
        GroupDevice groupDevice = new GroupDevice();
        groupDevice.setDeviceId(device.getId());
        groupDevice.setGroupId(id);
        daoSession.getGroupDeviceDao().insert(groupDevice);
    }

    public void setDeviceIds(long[] deviceIds) {
        Set<Long> deviceSet = new HashSet<>();
        for (long deviceId : deviceIds) {
            deviceSet.add(deviceId);
        }

        for (GroupDevice groupDevice : getGroupDeviceList()) {
            if (deviceSet.contains(groupDevice.getDeviceId())) {
                deviceSet.remove(groupDevice.getDeviceId());
            } else {
                groupDevice.delete();
            }
        }

        for (Long deviceId : deviceSet) {
            addDevice(deviceId);
        }
    }
    // KEEP METHODS END

}
