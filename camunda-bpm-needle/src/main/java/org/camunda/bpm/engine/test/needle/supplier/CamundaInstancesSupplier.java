package org.camunda.bpm.engine.test.needle.supplier;

import static de.holisticon.toolbox.needle.provider.DefaultInstanceInjectionProvider.providerFor;

import java.util.Set;

import org.camunda.bpm.engine.AuthorizationService;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.ManagementService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.impl.test.TestHelper;

import com.google.common.collect.Sets;

import de.akquinet.jbosscc.needle.injection.InjectionProvider;
import de.holisticon.toolbox.needle.provider.InjectionProviderInstancesSupplier;

/**
 * Supplier for camunda services. Holds processEngine internally and exposes all
 * services via {@link InjectionProvider}.
 * 
 * @author Jan Galinski, Holisticon AG
 */
public class CamundaInstancesSupplier implements InjectionProviderInstancesSupplier, ProcessEngine {

  public static CamundaInstancesSupplier camundaInstancesSupplier() {
    return camundaInstancesSupplier(CAMUNDA_CONFIG_RESOURCE);
  }

  /**
   * Create new instance with given configuration resource.
   * 
   * @param configurationResource
   *          name of the configuration file (e.g. activiti.cfg.xml)
   * @see TestHelper#getProcessEngine(String)
   * @see #CamundaEngineInstancesSupplier(ProcessEngine)
   */
  public static CamundaInstancesSupplier camundaInstancesSupplier(final String configurationResource) {
    return new CamundaInstancesSupplier(TestHelper.getProcessEngine(configurationResource));
  }

  public static final String CAMUNDA_CONFIG_RESOURCE = "camunda.cfg.xml";

  private final Set<InjectionProvider<?>> providers = Sets.newHashSet();
  /**
   * The standalone process engine.
   */

  private final ProcessEngine processEngine;

  private CamundaInstancesSupplier(final ProcessEngine processEngine) {
    this.processEngine = processEngine;

    providers.add(providerFor(processEngine));
    providers.add(providerFor(getRepositoryService()));
    providers.add(providerFor(getRuntimeService()));
    providers.add(providerFor(getFormService()));
    providers.add(providerFor(getTaskService()));
    providers.add(providerFor(getHistoryService()));
    providers.add(providerFor(getIdentityService()));
    providers.add(providerFor(getManagementService()));
    providers.add(providerFor(getAuthorizationService()));
  }

  @Override
  public Set<InjectionProvider<?>> get() {
    return providers;
  }

  @Override
  public RepositoryService getRepositoryService() {
    return processEngine.getRepositoryService();
  }

  @Override
  public RuntimeService getRuntimeService() {
    return processEngine.getRuntimeService();
  }

  @Override
  public FormService getFormService() {
    return processEngine.getFormService();
  }

  @Override
  public TaskService getTaskService() {
    return processEngine.getTaskService();
  }

  @Override
  public HistoryService getHistoryService() {
    return processEngine.getHistoryService();
  }

  @Override
  public IdentityService getIdentityService() {
    return processEngine.getIdentityService();
  }

  @Override
  public ManagementService getManagementService() {
    return processEngine.getManagementService();
  }

  @Deprecated
  @Override
  public void close() {
    throw new UnsupportedOperationException("close() is not supported on supplier, use engine directly");
  }

  @Override
  public String getName() {
    return processEngine.getName();
  }

  /**
   * @return current process engine
   */
  public ProcessEngine getProcessEngine() {
    return processEngine;
  }

  @Override
  public AuthorizationService getAuthorizationService() {
    return processEngine.getAuthorizationService();
  }

}